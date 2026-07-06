package com.ahmedyejam.mks.data.import.xlsx

import android.util.Base64
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.util.Locale
import java.util.zip.ZipFile
import javax.xml.parsers.DocumentBuilderFactory

data class SheetImageResolution(
    val addressImages: Map<String, String> = emptyMap(),
    val rowImages: Map<Int, String> = emptyMap()
)

class XlsxImageResolver {

    private val dbf = DocumentBuilderFactory.newInstance().apply {
        isNamespaceAware = true
    }

    fun extractDispImgId(formula: String): String? {
        val match = Regex("""(?:_xlfn\.)?DISPIMG\(\s*(?:N\s*)?(?:"|')?([^,"'&)<>{}\s]+)(?:"|')?""", RegexOption.IGNORE_CASE).find(formula)
        return match?.groupValues?.getOrNull(1)?.trim()?.takeIf { it.isNotBlank() }
    }

    fun getCellImagePathMap(zipFile: ZipFile): Map<String, String> {
        val cellImagesXml = readEntry(zipFile, "xl/cellimages.xml") ?: return emptyMap()
        val relsXml = readEntry(zipFile, "xl/_rels/cellimages.xml.rels") ?: return emptyMap()
        val relMap = parseRelationships(relsXml, "xl/cellimages.xml")
        val result = linkedMapOf<String, String>()
        try {
            val doc = parseXml(cellImagesXml)
            val picNodes = doc.getElementsByTagNameNS("*", "pic")
            for (index in 0 until picNodes.length) {
                val pic = picNodes.item(index) as? Element ?: continue
                val cNvPr = pic.getElementsByTagNameNS("*", "cNvPr").item(0) as? Element
                val blip = pic.getElementsByTagNameNS("*", "blip").item(0) as? Element
                val imageId = cNvPr?.getAttribute("name").orEmpty().trim()
                val relId = blip?.getAttributeNS("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "embed").orEmpty().ifBlank {
                    blip?.getAttribute("r:embed").orEmpty()
                }
                val targetPath = relMap[relId]
                if (imageId.isNotBlank() && !targetPath.isNullOrBlank()) result[imageId] = targetPath
            }
        } catch (_: Exception) {
        }
        return result
    }

    fun resolveSheetImages(zipFile: ZipFile, sheetName: String, cellImagePathMap: Map<String, String>): SheetImageResolution {
        val formulaImages = resolveSheetFormulaImages(zipFile, sheetName, cellImagePathMap)
        val anchoredImages = resolveSheetAnchoredImages(zipFile, sheetName)
        return SheetImageResolution(addressImages = anchoredImages.addressImages + formulaImages, rowImages = anchoredImages.rowImages)
    }

    fun resolveSheetFormulaImages(zipFile: ZipFile, sheetName: String, cellImagePathMap: Map<String, String>): Map<String, String> {
        val sheetPath = getSheetPath(zipFile, sheetName) ?: return emptyMap()
        val sheetBytes = readEntry(zipFile, sheetPath) ?: return emptyMap()
        val result = linkedMapOf<String, String>()
        val pathCache = mutableMapOf<String, String>()
        try {
            val doc = parseXml(sheetBytes)
            val cellNodes = doc.getElementsByTagName("c")
            for (index in 0 until cellNodes.length) {
                val cell = cellNodes.item(index) as? Element ?: continue
                val address = cell.getAttribute("r")
                if (address.isBlank()) continue
                val formulaText = cell.getElementsByTagName("f").item(0)?.textContent.orEmpty()
                val imageId = extractDispImgId(formulaText) ?: continue
                val zipPath = cellImagePathMap[imageId] ?: continue
                val dataUrl = pathCache.getOrPut(zipPath) { buildDataUrl(zipFile, zipPath).orEmpty() }
                if (dataUrl.isNotBlank()) result[address] = dataUrl
            }
        } catch (_: Exception) {
        }
        return result
    }

    fun resolveSheetAnchoredImages(zipFile: ZipFile, sheetName: String): SheetImageResolution {
        val sheetPath = getSheetPath(zipFile, sheetName) ?: return SheetImageResolution()
        val sheetRelsBytes = readEntry(zipFile, siblingRelsPath(sheetPath)) ?: return SheetImageResolution()
        val drawingPaths = parseRelationshipsWithType(sheetRelsBytes, sheetPath)
            .filter { (_, relation) -> relation.type.contains("/drawing") }
            .map { it.value.path }
            .distinct()
        if (drawingPaths.isEmpty()) return SheetImageResolution()

        val addressImages = linkedMapOf<String, String>()
        val rowImages = linkedMapOf<Int, String>()
        val pathCache = mutableMapOf<String, String>()

        drawingPaths.forEach { drawingPath ->
            val drawingBytes = readEntry(zipFile, drawingPath) ?: return@forEach
            val drawingRels = readEntry(zipFile, siblingRelsPath(drawingPath)) ?: return@forEach
            val drawingRelMap = parseRelationships(drawingRels, drawingPath)
            try {
                val drawingDoc = parseXml(drawingBytes)
                listOf("twoCellAnchor", "oneCellAnchor").forEach { tag ->
                    val nodes = drawingDoc.getElementsByTagNameNS("*", tag)
                    for (index in 0 until nodes.length) {
                        val anchor = nodes.item(index) as? Element ?: continue
                        val from = anchor.getElementsByTagNameNS("*", "from").item(0) as? Element ?: continue
                        val col = from.getElementsByTagNameNS("*", "col").item(0)?.textContent?.toIntOrNull() ?: continue
                        val row = from.getElementsByTagNameNS("*", "row").item(0)?.textContent?.toIntOrNull() ?: continue
                        val blip = anchor.getElementsByTagNameNS("*", "blip").item(0) as? Element ?: continue
                        val relId = blip.getAttributeNS("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "embed").ifBlank { blip.getAttribute("r:embed") }
                        val zipPath = drawingRelMap[relId] ?: continue
                        val dataUrl = pathCache.getOrPut(zipPath) { buildDataUrl(zipFile, zipPath).orEmpty() }
                        if (dataUrl.isBlank()) continue
                        val rowOneBased = row + 1
                        val cellRef = columnLetters(col) + rowOneBased
                        addressImages.putIfAbsent(cellRef, dataUrl)
                        rowImages.putIfAbsent(rowOneBased, dataUrl)
                    }
                }
            } catch (_: Exception) {
            }
        }

        return SheetImageResolution(addressImages = addressImages, rowImages = rowImages)
    }

    private fun buildDataUrl(zipFile: ZipFile, zipPath: String): String? {
        val bytes = readEntry(zipFile, zipPath) ?: return null
        return "data:${getImageMimeType(zipPath)};base64," + Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun parseRelationships(relsBytes: ByteArray, basePath: String): Map<String, String> {
        return parseRelationshipsWithType(relsBytes, basePath).mapValues { it.value.path }
    }

    private fun parseRelationshipsWithType(relsBytes: ByteArray, basePath: String): Map<String, RelationshipTarget> {
        val map = linkedMapOf<String, RelationshipTarget>()
        try {
            val doc = parseXml(relsBytes)
            val relNodes = doc.getElementsByTagName("Relationship")
            for (index in 0 until relNodes.length) {
                val rel = relNodes.item(index) as? Element ?: continue
                val id = rel.getAttribute("Id")
                val target = rel.getAttribute("Target")
                if (id.isBlank() || target.isBlank()) continue
                map[id] = RelationshipTarget(resolveZipPath(basePath, target), rel.getAttribute("Type"))
            }
        } catch (_: Exception) {
        }
        return map
    }

    private fun getSheetPath(zipFile: ZipFile, sheetName: String): String? {
        val workbookXml = readEntry(zipFile, "xl/workbook.xml") ?: return null
        val relsXml = readEntry(zipFile, "xl/_rels/workbook.xml.rels") ?: return null
        val relMap = parseRelationships(relsXml, "xl/workbook.xml")
        return try {
            val doc = parseXml(workbookXml)
            val sheetNodes = doc.getElementsByTagNameNS("*", "sheet")
            for (index in 0 until sheetNodes.length) {
                val sheet = sheetNodes.item(index) as? Element ?: continue
                if (sheet.getAttribute("name") != sheetName) continue
                val relId = sheet.getAttributeNS("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id").ifBlank { sheet.getAttribute("r:id") }
                return relMap[relId]
            }
            null
        } catch (_: Exception) {
            null
        }
    }

    private fun siblingRelsPath(path: String): String {
        val dir = path.substringBeforeLast('/', "")
        val name = path.substringAfterLast('/')
        return if (dir.isBlank()) "_rels/$name.rels" else "$dir/_rels/$name.rels"
    }

    private fun resolveZipPath(basePath: String, target: String): String {
        val baseDir = basePath.substringBeforeLast('/', "")
        val combined = if (baseDir.isBlank()) target else "$baseDir/$target"
        val resolved = mutableListOf<String>()
        combined.split('/').forEach { part ->
            when (part) {
                "", "." -> Unit
                ".." -> if (resolved.isNotEmpty()) resolved.removeAt(resolved.lastIndex)
                else -> resolved += part
            }
        }
        return resolved.joinToString("/")
    }

    private fun readEntry(zipFile: ZipFile, name: String): ByteArray? = try {
        zipFile.getEntry(name)?.let { entry -> zipFile.getInputStream(entry).use { it.readBytes() } }
    } catch (_: Exception) {
        null
    }

    private fun parseXml(bytes: ByteArray): Document = dbf.newDocumentBuilder().parse(bytes.inputStream())

    private fun getImageMimeType(path: String): String = when (path.substringAfterLast('.', "png").lowercase(Locale.ROOT)) {
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "bmp" -> "image/bmp"
        "svg" -> "image/svg+xml"
        else -> "image/png"
    }

    private fun columnLetters(zeroBasedColumn: Int): String {
        var value = zeroBasedColumn + 1
        var letters = ""
        while (value > 0) {
            val remainder = (value - 1) % 26
            letters = ('A'.code + remainder).toChar() + letters
            value = (value - 1) / 26
        }
        return letters
    }

    private data class RelationshipTarget(val path: String, val type: String)
}
