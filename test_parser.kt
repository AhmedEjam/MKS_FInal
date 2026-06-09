import com.ahmedyejam.mks.data.import.parser.TextFlashcardParser
import com.ahmedyejam.mks.data.import.parser.TextParseMode

fun main() {
    val parser = TextFlashcardParser()
    val text = "Front 1\n\nBack 1\n\n\n\nFront 2\n\nBack 2\n\n \n\nFront 3"
    val res = parser.parse(text, 1L, 0, TextParseMode.ALTERNATING_PARAGRAPHS)
    res.forEach { println("Front: ${it.frontText}, Back: ${it.backText}") }
}
