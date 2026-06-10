package com.ahmedyejam.mks.data.import.parser

import com.ahmedyejam.mks.data.local.entity.FlashcardEntity
import java.util.UUID

enum class TextParseMode {
    ALTERNATING_PARAGRAPHS,
    EXPLICIT_LABELS,
    HEADER_BODY_NOTES
}

class TextFlashcardParser {
    fun parse(text: String, deckId: Long, startIndex: Int = 0, mode: TextParseMode = TextParseMode.ALTERNATING_PARAGRAPHS): List<FlashcardEntity> {
        val result = mutableListOf<FlashcardEntity>()
        
        val paragraphs = text.split(Regex("\\n\\s*\\n")).map { it.trim() }.filter { it.isNotEmpty() }
        var order = startIndex

        if (mode == TextParseMode.ALTERNATING_PARAGRAPHS) {
            var i = 0
            while (i < paragraphs.size) {
                val frontParagraph = paragraphs[i]
                var backParagraph = ""
                if (i + 1 < paragraphs.size) {
                    backParagraph = paragraphs[i+1]
                    i += 2
                } else {
                    break
                }
                
                val frontClean = frontParagraph.replace(Regex("^(Front|Q|Question):\\s*", RegexOption.IGNORE_CASE), "").trim()
                val backClean = backParagraph.replace(Regex("^(Back|A|Answer):\\s*", RegexOption.IGNORE_CASE), "").trim()
                
                result.add(
                    FlashcardEntity(
                        externalId = UUID.randomUUID().toString(),
                        deckId = deckId,
                        frontText = frontClean,
                        backText = backClean,
                        orderIndex = order++
                    )
                )
            }
        } else {
            // Explicit Labels Mode
            for (paragraph in paragraphs) {
                // We expect each paragraph block to contain both a Front and a Back label
                val frontMatch = Regex("^(?:Front|Q|Question):\\s*(.*?)(?:\\n(?:Back|A|Answer):|$)", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)).find(paragraph)
                val backMatch = Regex("\\n(?:Back|A|Answer):\\s*(.*)$", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)).find(paragraph)

                if (frontMatch != null && backMatch != null) {
                    result.add(
                        FlashcardEntity(
                            externalId = UUID.randomUUID().toString(),
                            deckId = deckId,
                            frontText = frontMatch.groupValues.getOrNull(1)?.trim() ?: "",
                            backText = backMatch.groupValues.getOrNull(1)?.trim() ?: "",
                            orderIndex = order++
                        )
                    )
                } else {
                    // Fallback if labels not found: treat the whole block as front
                    result.add(
                        FlashcardEntity(
                            externalId = UUID.randomUUID().toString(),
                            deckId = deckId,
                            frontText = paragraph.trim(),
                            backText = "",
                            orderIndex = order++
                        )
                    )
                }
            }
        }
        return result
    }
}
