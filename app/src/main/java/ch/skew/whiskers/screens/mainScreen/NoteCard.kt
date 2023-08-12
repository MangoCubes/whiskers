package ch.skew.whiskers.screens.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.skew.whiskers.functions.emojiString
import ch.skew.whiskers.functions.modifiers.fadingEdge
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.api.Emoji
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    note: Note,
    noteScreen: () -> Unit,
    emojiMap: Map<String, Emoji>
) {
    val requestSent = remember { mutableStateOf(false) }
    val bottomFade = Brush.verticalGradient(0.6f to Color.Red, 1f to Color.Transparent)
    val inlineContent = remember { mutableStateMapOf<String, InlineTextContent>() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun requestEmojis(emojiList: List<String>) {
        if(requestSent.value) return
        else {
            emojiList.forEach {
                val emoji = emojiMap[it] ?: return@forEach
                scope.launch {
                    val image = ImageRequest.Builder(context)
                        .data(emoji.url)
                        .memoryCacheKey(emoji.name)
                        .diskCacheKey(emoji.name)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .crossfade(true)
                        .build()
                    context.imageLoader.enqueue(image)
                    inlineContent[emoji.name] = InlineTextContent(
                        Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
                    ) {
                        AsyncImage(model = image, contentDescription = emoji.name)
                    }
                }
            }
            requestSent.value = true
        }
    }

    Card(
        onClick = noteScreen,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(0.dp, 250.dp)
    ) {
        val overflow = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    painter = rememberAsyncImagePainter(note.user.avatarUrl),
                    contentDescription = note.user.username
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    note.user.name?.let { name ->
                        val (found, annotatedString) = emojiString(name)
                        found.distinct().let {
                            if(it.isNotEmpty()) requestEmojis(it)
                        }
                        Text(
                            annotatedString,
                            inlineContent = inlineContent
                        )
                    } ?: Text(
                        "@" + note.user.username,
                        style = MaterialTheme.typography.titleMedium
                    )
                    val createdAt = LocalDateTime.parse(note.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    Text(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                }
            }
            note.text?.let { content ->
                val (found, annotatedString) = emojiString(content)
                found.distinct().let {
                    if(it.isNotEmpty()) requestEmojis(it)
                }
                Text(
                    annotatedString,
                    onTextLayout = { result ->
                        if(result.didOverflowHeight) overflow.value = true
                    },
                    modifier = if (overflow.value) Modifier.fadingEdge(bottomFade) else Modifier,
                    inlineContent = inlineContent
                )
            }
        }
    }
}