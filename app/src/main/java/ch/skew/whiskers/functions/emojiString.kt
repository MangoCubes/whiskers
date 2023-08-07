package ch.skew.whiskers.functions

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString

fun emojiString(original: String): AnnotatedString {
    var removed: String = original
    val pattern = ":([A-z_0-9]+):".toRegex()
    return buildAnnotatedString {
        while (true) {
            val match = pattern.find(removed)
            if(match !== null) {
                val (name) = match.destructured
                append(removed.slice(0 until match.range.first))
                removed = removed.slice(match.range.last + 1 until removed.length)
                appendInlineContent(name, alternateText = match.value)
            } else {
                append(removed)
                break
            }
        }
    }
}