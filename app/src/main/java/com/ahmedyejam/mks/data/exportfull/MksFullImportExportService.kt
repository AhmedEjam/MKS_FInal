package com.ahmedyejam.mks.data.exportfull

import android.content.Context
import android.net.Uri
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.simulation.ChangeSimulationResult
import com.ahmedyejam.mks.data.simulation.SimulatedItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

data class MksFullExportResult(
    val success: Boolean,
    val file: File? = null,
    val warnings: List<String> = emptyList(),
    val error: String? = null
)

class MksFullImportExportService(
    private val context: Context,
    private val database: MksDatabase
) {
    private val exportTables = listOf(
        "books",
        "quizzes",
        "questions",
        "sessions",
        "question_categories",
        "asset_references",
        "question_assets",
        "source_documents",
        "flashcard_decks",
        "flashcards",
        "note_blueprints",
        "slideshow_courses",
        "course_slides",
        "prompts",
        "prompt_decks",
        "prompt_cards",
        "prompt_runs",
        "knowledge_study_sessions",
        "learning_sessions",
        "mistake_log_entries"
    )

    suspend fun exportFullLibrary(): MksFullExportResult = withContext(Dispatchers.IO) {
        runCatching {
            val outDir = File(context.cacheDir, "mks_exports").apply { mkdirs() }
            val file = File(outDir, "mks_full_export_${System.currentTimeMillis()}.zip")
            val manifest = JSONObject()
                .put("appName", "MKS")
                .put("exportVersion", 1)
                .put("databaseVersion", MksDatabase.DB_VERSION)
                .put("createdAt", System.currentTimeMillis())
                .put("includesMedia", false)
            val warnings = mutableListOf<String>()
            ZipOutputStream(file.outputStream()).use { zip ->
                zip.putNextEntry(ZipEntry("manifest.json"))
                zip.write(manifest.toString(2).toByteArray())
                zip.closeEntry()
                exportTables.forEach { table ->
                    val json = runCatching { readTableAsJson(table) }
                        .onFailure { warnings += "Could not export $table: ${it.message}" }
                        .getOrDefault(JSONArray())
                    zip.putNextEntry(ZipEntry("data/$table.json"))
                    zip.write(json.toString(2).toByteArray())
                    zip.closeEntry()
                }
            }
            MksFullExportResult(success = true, file = file, warnings = warnings)
        }.getOrElse { MksFullExportResult(success = false, error = it.message ?: "Export failed") }
    }

    suspend fun simulateImportBundle(uri: Uri): ChangeSimulationResult = withContext(Dispatchers.IO) {
        val file = try {
            val tempDir = File(context.cacheDir, "mks_import_previews").apply { mkdirs() }
            val tempFile = File(tempDir, "preview_${System.currentTimeMillis()}.zip")
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: error("Could not open selected import file.")
            inputStream.use { input ->
                tempFile.outputStream().use { output -> input.copyTo(output) }
            }
            tempFile
        } catch (error: Exception) {
            return@withContext ChangeSimulationResult(
                title = "Import bundle preview",
                summary = "Could not read the selected import file.",
                blockedItems = listOf(SimulatedItem(uri.toString(), "File", "Selected import file", reason = error.message))
            )
        }
        simulateImportBundle(file)
    }

    suspend fun simulateImportBundle(file: File): ChangeSimulationResult = withContext(Dispatchers.IO) {
        if (!file.exists()) {
            return@withContext ChangeSimulationResult(
                title = "Import bundle preview",
                summary = "The selected file was not found.",
                blockedItems = listOf(SimulatedItem(file.absolutePath, "File", file.name, reason = "Missing file"))
            )
        }
        runCatching {
            ZipFile(file).use { zip ->
                val manifest = zip.getEntry("manifest.json")
                val entries = zip.entries().asSequence().filter { !it.isDirectory }.toList()
                val dataEntries = entries.filter { it.name.startsWith("data/") && it.name.endsWith(".json") }
                val warnings = mutableListOf<String>()
                if (manifest == null) warnings += "No manifest.json found; only preview is available."
                val created = dataEntries.map { entry ->
                    val count = runCatching {
                        zip.getInputStream(entry).bufferedReader().use { JSONArray(it.readText()).length() }
                    }.getOrDefault(0)
                    SimulatedItem(entry.name, "Import data", entry.name.removePrefix("data/").removeSuffix(".json"), "$count records")
                }
                ChangeSimulationResult(
                    title = "Import bundle preview",
                    summary = "This preview reads the bundle safely before any data is changed. Import-as-new-book should be confirmed before applying.",
                    warnings = warnings + "Apply/import is intentionally conservative: use existing legacy importer for older quiz bundles; full graph merge requires preview confirmation.",
                    createdItems = created
                )
            }
        }.getOrElse {
            ChangeSimulationResult(
                title = "Import bundle preview",
                summary = "Could not read import bundle.",
                blockedItems = listOf(SimulatedItem(file.absolutePath, "File", file.name, reason = it.message))
            )
        }
    }

    suspend fun applyImportBundle(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        val file = try {
            val tempFile = File(context.cacheDir, "mks_import_apply.zip")
            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output -> input.copyTo(output) }
            }
            tempFile
        } catch (e: Exception) {
            return@withContext false
        }
        
        if (!file.exists()) return@withContext false
        
        runCatching {
            database.runInTransaction {
                ZipFile(file).use { zip ->
                    exportTables.forEach { table ->
                        val entry = zip.getEntry("data/$table.json") ?: return@forEach
                        val jsonArray = zip.getInputStream(entry).bufferedReader().use { JSONArray(it.readText()) }
                        if (jsonArray.length() > 0) {
                            applyTableJson(table, jsonArray)
                        }
                    }
                }
            }
            file.delete()
            true
        }.getOrDefault(false)
    }

    private fun applyTableJson(table: String, data: JSONArray) {
        val db = database.openHelper.writableDatabase
        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val contentValues = android.content.ContentValues()
            val keys = obj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = obj.get(key)
                if (value == JSONObject.NULL) {
                    contentValues.putNull(key)
                } else when (value) {
                    is String -> contentValues.put(key, value)
                    is Long -> contentValues.put(key, value)
                    is Int -> contentValues.put(key, value)
                    is Double -> contentValues.put(key, value)
                    is Boolean -> contentValues.put(key, if (value) 1 else 0)
                }
            }
            db.insert(table, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, contentValues)
        }
    }

    private fun readTableAsJson(table: String): JSONArray {
        val db = database.openHelper.readableDatabase
        val cursor = db.query("SELECT * FROM $table")
        cursor.use {
            val array = JSONArray()
            while (it.moveToNext()) {
                val obj = JSONObject()
                for (i in 0 until it.columnCount) {
                    val name = it.getColumnName(i)
                    when (it.getType(i)) {
                        android.database.Cursor.FIELD_TYPE_NULL -> obj.put(name, JSONObject.NULL)
                        android.database.Cursor.FIELD_TYPE_INTEGER -> obj.put(name, it.getLong(i))
                        android.database.Cursor.FIELD_TYPE_FLOAT -> obj.put(name, it.getDouble(i))
                        android.database.Cursor.FIELD_TYPE_STRING -> obj.put(name, it.getString(i))
                        android.database.Cursor.FIELD_TYPE_BLOB -> obj.put(name, "<blob:${it.getBlob(i).size} bytes>")
                    }
                }
                array.put(obj)
            }
            return array
        }
    }
}
