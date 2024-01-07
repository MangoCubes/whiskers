package ch.skew.whiskers.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.misskey.data.api.Emoji

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ReactionSelectorDialogPreview() {
    AlertDialog(onDismissRequest = { }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Select a reaction")
            ReactionSelector(mapOf())
        }
    }
}

@Composable
fun ReactionSelectorSection(title: String, emojis: List<ByteArray>) {
    val state = rememberScrollState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(30.dp),
        modifier = Modifier.verticalScroll(state, true)
            .height(500.dp)
    ) {
        items(emojis) { i ->
            TextButton(
                onClick = { /*TODO*/ }
            ) {
                Text(String(i, Charsets.UTF_32))
            }
        }
    }
}

fun uintToBytes(value: UInt): ByteArray {
    val bytes = ByteArray(4)
    bytes[3] = (value and 0xFFU).toByte()
    bytes[2] = ((value shr 8) and 0xFFU).toByte()
    bytes[1] = ((value shr 16) and 0xFFU).toByte()
    bytes[0] = ((value shr 24) and 0xFFU).toByte()
    return bytes
}

val smileysAndEmoticons = listOf(
    listOf(0x1f600),
    listOf(0x1f603),
    listOf(0x1f604),
    listOf(0x1f601),
    listOf(0x1f606),
    listOf(0x1f605),
    listOf(0x1f923),
    listOf(0x1f602),
    listOf(0x1f642),
    listOf(0x1f643),
    listOf(0x1fae0),
    listOf(0x1f609),
    listOf(0x1f60a),
    listOf(0x1f607),
    listOf(0x1f970),
    listOf(0x1f60d),
    listOf(0x1f929),
    listOf(0x1f618),
    listOf(0x1f617),
    listOf(0x263a),
    listOf(0x1f61a),
    listOf(0x1f619),
    listOf(0x1f972),
    listOf(0x1f60b),
    listOf(0x1f61b),
    listOf(0x1f61c),
    listOf(0x1f92a),
    listOf(0x1f61d),
    listOf(0x1f911),
    listOf(0x1f917),
    listOf(0x1f92d),
    listOf(0x1fae2),
    listOf(0x1fae3),
    listOf(0x1f92b),
    listOf(0x1f914),
    listOf(0x1fae1),
    listOf(0x1f910),
    listOf(0x1f928),
    listOf(0x1f610),
    listOf(0x1f611),
    listOf(0x1f636),
    listOf(0x1fae5),
    listOf(0x1f60f),
    listOf(0x1f612),
    listOf(0x1f644),
    listOf(0x1f62c),
    listOf(0x1f925),
    listOf(0x1f60c),
    listOf(0x1f614),
    listOf(0x1f62a),
    listOf(0x1f924),
    listOf(0x1f634),
    listOf(0x1f637),
    listOf(0x1f912),
    listOf(0x1f915),
    listOf(0x1f922),
    listOf(0x1f92e),
    listOf(0x1f927),
    listOf(0x1f975),
    listOf(0x1f976),
    listOf(0x1f974),
    listOf(0x1f635),
    listOf(0x1f92f),
    listOf(0x1f920),
    listOf(0x1f973),
    listOf(0x1f978),
    listOf(0x1f60e),
    listOf(0x1f913),
    listOf(0x1f9d0),
    listOf(0x1f615),
    listOf(0x1fae4),
    listOf(0x1f61f),
    listOf(0x1f641),
    listOf(0x2639),
    listOf(0x1f62e),
    listOf(0x1f62f),
    listOf(0x1f632),
    listOf(0x1f633),
    listOf(0x1f97a),
    listOf(0x1f979),
    listOf(0x1f626),
    listOf(0x1f627),
    listOf(0x1f628),
    listOf(0x1f630),
    listOf(0x1f625),
    listOf(0x1f622),
    listOf(0x1f62d),
    listOf(0x1f631),
    listOf(0x1f616),
    listOf(0x1f623),
    listOf(0x1f61e),
    listOf(0x1f613),
    listOf(0x1f629),
    listOf(0x1f62b),
    listOf(0x1f971),
    listOf(0x1f624),
    listOf(0x1f621),
    listOf(0x1f620),
    listOf(0x1f92c),
    listOf(0x1f608),
    listOf(0x1f47f),
    listOf(0x1f480),
    listOf(0x2620),
    listOf(0x1f4a9),
    listOf(0x1f921),
    listOf(0x1f479),
    listOf(0x1f47a),
    listOf(0x1f47b),
    listOf(0x1f47d),
    listOf(0x1f47e),
    listOf(0x1f916),
    listOf(0x1f63a),
    listOf(0x1f638),
    listOf(0x1f639),
    listOf(0x1f63b),
    listOf(0x1f63c),
    listOf(0x1f63d),
    listOf(0x1f640),
    listOf(0x1f63f),
    listOf(0x1f63e),
    listOf(0x1f648),
    listOf(0x1f649),
    listOf(0x1f64a),
    listOf(0x1f48c),
    listOf(0x1f498),
    listOf(0x1f49d),
    listOf(0x1f496),
    listOf(0x1f497),
    listOf(0x1f493),
    listOf(0x1f49e),
    listOf(0x1f495),
    listOf(0x1f49f),
    listOf(0x2763),
    listOf(0x1f494),
    listOf(0x2764),
    listOf(0x1f9e1),
    listOf(0x1f49b),
    listOf(0x1f49a),
    listOf(0x1f499),
    listOf(0x1f49c),
    listOf(0x1f90e),
    listOf(0x1f5a4),
    listOf(0x1f90d),
    listOf(0x1f48b),
    listOf(0x1f4af),
    listOf(0x1f4a2),
    listOf(0x1f4a5),
    listOf(0x1f4ab),
    listOf(0x1f4a6),
    listOf(0x1f4a8),
    listOf(0x1f573),
    listOf(0x1f4ac),
    listOf(0x1f5e8),
    listOf(0x1f5ef),
    listOf(0x1f4ad),
    listOf(0x1f4a4),
    listOf(0x2764, 0xFE0F, 0x200D, 0x1F525),
    listOf(0x1f441, 0xfe0f, 0x200d, 0x1f5e8, 0xfe0f),
    listOf(0x2764, 0xfe0f, 0x200d, 0x1f525),
    listOf(0x2764, 0xfe0f, 0x200d, 0x1fa79),
    listOf(0x1f635, 0x200d, 0x1f4ab),
    listOf(0x1f62e, 0x200d, 0x1f4a8),
    listOf(0x1f636, 0x200d, 0x1f32b, 0xfe0f),
).map { emoji ->
    emoji.map { uintToBytes(it.toUInt()) }.reduce { a, b -> a + b }
}

@Composable
fun ReactionSelector(reactions: Map<String, Emoji>) {
    ReactionSelectorSection(title = "Smileys and emoticons", emojis = smileysAndEmoticons)
}