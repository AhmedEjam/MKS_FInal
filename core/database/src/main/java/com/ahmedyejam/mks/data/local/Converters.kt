package com.ahmedyejam.mks.data.local

import androidx.room.TypeConverter
import com.ahmedyejam.mks.data.local.entity.QuestionType
import com.ahmedyejam.mks.util.MksLogger
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

class Converters {
    private val moshi = Moshi.Builder().build()
    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val intListType = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
    private val longListType = Types.newParameterizedType(List::class.java, Long::class.javaObjectType)

    private val listIntType =
        Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
    private val mapType =
        Types.newParameterizedType(Map::class.java, Long::class.javaObjectType, listIntType)
    private val mapNotesType =
        Types.newParameterizedType(Map::class.java, Long::class.javaObjectType, String::class.java)
    private val mapIntType = Types.newParameterizedType(
        Map::class.java,
        Long::class.javaObjectType,
        Int::class.javaObjectType
    )
    private val mapIntIntType = Types.newParameterizedType(
        Map::class.java,
        Int::class.javaObjectType,
        Int::class.javaObjectType
    )
    private val mapIntListIntType =
        Types.newParameterizedType(Map::class.java, Int::class.javaObjectType, listIntType)
    private val mapIntStringType =
        Types.newParameterizedType(Map::class.java, Int::class.javaObjectType, String::class.java)
    private val mapStringType: ParameterizedType = Types.newParameterizedType(
        Map::class.java,
        String::class.java,
        java.lang.Boolean::class.javaObjectType
    )

    private val stringListAdapter = moshi.adapter<List<String>>(stringListType)
    private val intListAdapter = moshi.adapter<List<Int>>(intListType)
    private val longListAdapter = moshi.adapter<List<Long>>(longListType)

    private val answersMapAdapter = moshi.adapter<Map<Long, List<Int>>>(mapType)
    private val notesMapAdapter = moshi.adapter<Map<Long, String>>(mapNotesType)
    private val intMapAdapter = moshi.adapter<Map<Long, Int>>(mapIntType)
    private val intIntMapAdapter = moshi.adapter<Map<Int, Int>>(mapIntIntType)
    private val intListIntMapAdapter = moshi.adapter<Map<Int, List<Int>>>(mapIntListIntType)
    private val intStringMapAdapter = moshi.adapter<Map<Int, String>>(mapIntStringType)
    private val stringMapAdapter = moshi.adapter<Map<String, Boolean>>(mapStringType)

    private val TAG = "Converters"

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return try {
            stringListAdapter.toJson(value ?: emptyList())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize string list", e)
            "[]"
        }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            if (value.startsWith("[")) {
                stringListAdapter.fromJson(value) ?: emptyList()
            } else {
                value.split(",").map { it.trim() }.filter { it.isNotBlank() }
            }
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse string list: $value", e)
            emptyList()
        }
    }

    @TypeConverter
    fun fromIntList(value: List<Int>?): String {
        return try {
            intListAdapter.toJson(value ?: emptyList())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize int list", e)
            "[]"
        }
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            if (value.startsWith("[")) {
                intListAdapter.fromJson(value) ?: emptyList()
            } else {
                value.split(",").mapNotNull { it.trim().toIntOrNull() }
            }
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse int list: $value", e)
            emptyList()
        }
    }

    @TypeConverter
    fun fromLongList(value: List<Long>?): String {
        return try {
            longListAdapter.toJson(value ?: emptyList())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize long list", e)
            "[]"
        }
    }

    @TypeConverter
    fun toLongList(value: String?): List<Long> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            if (value.startsWith("[")) {
                longListAdapter.fromJson(value) ?: emptyList()
            } else {
                value.split(",").mapNotNull { it.trim().toLongOrNull() }
            }
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse long list: $value", e)
            emptyList()
        }
    }

    @TypeConverter
    fun fromAnswersMap(value: Map<Long, List<Int>>?): String {
        return try {
            answersMapAdapter.toJson(value ?: emptyMap())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize answers map", e)
            "{}"
        }
    }

    @TypeConverter
    fun toAnswersMap(value: String?): Map<Long, List<Int>> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            answersMapAdapter.fromJson(value) ?: emptyMap()
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse answers map: $value", e)
            emptyMap()
        }
    }

    @TypeConverter
    fun fromNotesMap(value: Map<Long, String>?): String {
        return try {
            notesMapAdapter.toJson(value ?: emptyMap())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize notes map", e)
            "{}"
        }
    }

    @TypeConverter
    fun toNotesMap(value: String?): Map<Long, String> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            notesMapAdapter.fromJson(value) ?: emptyMap()
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse notes map: $value", e)
            emptyMap()
        }
    }

    @TypeConverter
    fun fromIntMap(value: Map<Long, Int>?): String {
        return try {
            intMapAdapter.toJson(value ?: emptyMap())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize int map", e)
            "{}"
        }
    }

    @TypeConverter
    fun toIntMap(value: String?): Map<Long, Int> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            intMapAdapter.fromJson(value) ?: emptyMap()
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse int map: $value", e)
            emptyMap()
        }
    }

    @TypeConverter
    fun fromIntIntMap(value: Map<Int, Int>?): String {
        return try {
            intIntMapAdapter.toJson(value ?: emptyMap())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize int-int map", e)
            "{}"
        }
    }

    @TypeConverter
    fun toIntIntMap(value: String?): Map<Int, Int> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            intIntMapAdapter.fromJson(value) ?: emptyMap()
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse int-int map: $value", e)
            emptyMap()
        }
    }

    @TypeConverter
    fun fromIntListIntMap(value: Map<Int, List<Int>>?): String {
        return try {
            intListIntMapAdapter.toJson(value ?: emptyMap())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize int-list-int map", e)
            "{}"
        }
    }

    @TypeConverter
    fun toIntListIntMap(value: String?): Map<Int, List<Int>> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            intListIntMapAdapter.fromJson(value) ?: emptyMap()
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse int-list-int map: $value", e)
            emptyMap()
        }
    }

    @TypeConverter
    fun fromIntStringMap(value: Map<Int, String>?): String {
        return try {
            intStringMapAdapter.toJson(value ?: emptyMap())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize int-string map", e)
            "{}"
        }
    }

    @TypeConverter
    fun toIntStringMap(value: String?): Map<Int, String> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            intStringMapAdapter.fromJson(value) ?: emptyMap()
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse int-string map: $value", e)
            emptyMap()
        }
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, Boolean>?): String {
        return try {
            stringMapAdapter.toJson(map ?: emptyMap())
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to serialize string map", e)
            "{}"
        }
    }

    @TypeConverter
    fun toStringMap(value: String?): Map<String, Boolean> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            stringMapAdapter.fromJson(value) ?: emptyMap()
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to parse string map: $value", e)
            emptyMap()
        }
    }

    @TypeConverter
    fun fromQuestionType(value: QuestionType): String {
        return value.name
    }

    @TypeConverter
    fun toQuestionType(value: String): QuestionType {
        return try {
            QuestionType.valueOf(value)
        } catch (e: Exception) {
            MksLogger.w(TAG, "Unknown QuestionType: $value, defaulting to SINGLE_CHOICE")
            QuestionType.SINGLE_CHOICE
        }
    }
}
