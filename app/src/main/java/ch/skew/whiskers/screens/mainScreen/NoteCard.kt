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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.modifiers.fadingEdge
import coil.compose.rememberAsyncImagePainter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    note: Note,
    noteScreen: () -> Unit
) {
    val bottomFade = Brush.verticalGradient(0.6f to Color.Red, 1f to Color.Transparent)
    Card(
        onClick = noteScreen,
        modifier = Modifier.fillMaxWidth()
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
                    Text(
                        note.user.name?.let {
                            "$it (@" + note.user.username + ")"
                        } ?: ("@" + note.user.username),
                        style = MaterialTheme.typography.titleMedium
                    )
                    val createdAt = LocalDateTime.parse(note.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    Text(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                }
            }
            note.text?.let {
                Text(
                    it,
                    onTextLayout = { result ->
                        if(result.didOverflowHeight) overflow.value = true
                    },
                    modifier = if (overflow.value) Modifier.fadingEdge(bottomFade) else Modifier
                )
            }
        }
    }
}