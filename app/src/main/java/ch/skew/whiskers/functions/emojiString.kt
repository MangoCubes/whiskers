package ch.skew.whiskers.functions

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString

data class EmojiStringResult(val emojis: List<String>, val string: AnnotatedString)
fun emojiString(original: String): EmojiStringResult {
    var removed: String = original
    val pattern = ":([A-z_0-9]+):".toRegex()
    val emojis = mutableListOf<String>()
    val annotated = buildAnnotatedString {
        while (true) {
            val match = pattern.find(removed)
            if(match !== null) {
                val (name) = match.destructured
                emojis.add(name)
                append(removed.slice(0 until match.range.first))
                removed = removed.slice(match.range.last + 1 until removed.length)
                appendInlineContent(name, alternateText = match.value)
            } else {
                append(removed)
                break
            }
        }
    }
    return EmojiStringResult(emojis, annotated)
}