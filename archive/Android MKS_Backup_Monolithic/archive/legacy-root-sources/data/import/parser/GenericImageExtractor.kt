package com.ahmedyejam.mks.data.import.parser

import com.ahmedyejam.mks.data.import.model.ResolvedImage

class GenericImageExtractor {

    fun extractFromText(text: String?): ResolvedImage? {
        val raw = text?.trim() ?: return null
        if (raw.isEmpty()) return null

        Regex("""(data:image/[a-zA-Z0-9.+-]+;base64,[A-Za-z0-9+/=\s]+)""", RegexOption.IGNORE_CASE)
            .find(raw)?.let { match ->
                return ResolvedImage(imageDataUrl = match.groupValues[1].replace(Regex("""\s+"""), ""), via = "direct-data-url")
            }

        Regex("""<img[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
            .find(raw)?.let { match ->
                val src = match.groupValues[1].trim()
                return if (src.startsWith("data:", ignoreCase = true)) ResolvedImage(imageDataUrl = src, via = "html-img")
                else ResolvedImage(imageSource = src, via = "html-img")
            }

        Regex("""!\[[^\]]*]\(([^)]+)\)""").find(raw)?.let { match ->
            val src = match.groupValues[1].trim()
            return if (src.startsWith("data:", ignoreCase = true)) ResolvedImage(imageDataUrl = src, via = "markdown-img")
            else ResolvedImage(imageSource = src, via = "markdown-img")
        }

        Regex("""(https?://[^\s"'<>]+(?:\?[^\s"'<>]*)?)""", RegexOption.IGNORE_CASE)
            .find(raw)?.let { match ->
                return ResolvedImage(imageSource = match.groupValues[1], via = "direct-url")
            }

        Regex("""(?i)(?:^|[\s:=])((?:/?[\w\-. /\]+)\.(?:png|jpe?g|gif|webp|bmp|svg|tiff?|avif|ico))(?:$|[\s])""")
            .find(raw)?.let { match ->
                return ResolvedImage(imageSource = match.groupValues[1].trim().replace('\', '/'), via = "file-like")
            }

        return null
    }

    fun extractFromFields(text: String?): ResolvedImage? {
        val raw = text ?: return null
        val prefixes = listOf("image:", "image url:", "img:", "picture:", "media:", "image source:", "صورة:", "رابط الصورة:", "مصدر الصورة:")
        raw.lineSequence().forEach { line ->
            val trimmed = line.trim()
            prefixes.firstOrNull { trimmed.startsWith(it, ignoreCase = true) }?.let { prefix ->
                val value = trimmed.substring(prefix.length).trim()
                return extractFromText(value) ?: ResolvedImage(imageSource = value, via = "field-match")
            }
        }
        return null
    }
}
