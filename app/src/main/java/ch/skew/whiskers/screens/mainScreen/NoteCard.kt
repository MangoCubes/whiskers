package ch.skew.whiskers.screens.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.skew.whiskers.functions.emojiString
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.Visibility
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
    val inlineContent = remember { mutableStateMapOf<String, InlineTextContent>() }
    val context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun requestEmojis(emojiList: List<String>) {
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
    }

    Card(
        onClick = noteScreen,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(0.dp, if (expanded.value) Dp.Unspecified else 300.dp)
    ) {
        val overflow = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    painter = rememberAsyncImagePainter(note.user.avatarUrl),
                    contentDescription = note.user.username
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.weight(1F)
                ) {
                    note.user.name?.let { name ->
                        val (found, annotatedString) = emojiString(name)
                        found.distinct().let {
                            if(it.isNotEmpty()) requestEmojis(it)
                        }
                        Text(
                            buildAnnotatedString {
                                append(annotatedString)
                                append(" (@${note.user.username})")
                            },
                            inlineContent = inlineContent,
                            style = MaterialTheme.typography.titleMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    } ?: Text(
                        "@" + note.user.username,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    val createdAt = LocalDateTime.parse(note.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    Text(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                }
                Icon(
                    when(note.visibility) {
                        Visibility.Public -> Icons.Filled.Public
                        Visibility.Followers -> Icons.Filled.Lock
                        Visibility.Home -> Icons.Filled.Home
                        Visibility.Direct -> Icons.Filled.Mail
                    },
                    note.visibility.toString()
                )
            }
            note.text?.let { content ->
                val (found, annotatedString) = emojiString(content)
                found.distinct().let {
                    if(it.isNotEmpty()) requestEmojis(it)
                }
                if(expanded.value) {
                    Text(
                        annotatedString,
                        inlineContent = inlineContent
                    )
                } else {
                    Box(
                        if (overflow.value)
                            Modifier.weight(1F)
                                .fillMaxWidth()
                        else Modifier,
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Text(
                            annotatedString,
                            onTextLayout = { result ->
                                overflow.value = result.didOverflowHeight
                            },
                            inlineContent = inlineContent
                        )
                        if(overflow.value) {
                            Button(
                                { expanded.value = true },
                                elevation = ButtonDefaults.elevatedButtonElevation()
                            ) {
                                Text("Expand")
                            }
                        }
                    }
                }
            }
        }
    }
}