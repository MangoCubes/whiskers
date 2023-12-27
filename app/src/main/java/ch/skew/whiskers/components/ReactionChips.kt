package ch.skew.whiskers.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReactionChips(
    reactions: Map<String, Int>,
    maxHeight: Int,
    expanded: Boolean,
    enableAdd: Boolean,
    toggleReaction: (String) -> Unit
) {
    FlowRow(
        if (expanded) Modifier.heightIn(max = maxHeight.dp)
        else Modifier
    ) {
        reactions.map { entry ->
            AssistChip(
                onClick = { toggleReaction(entry.key) },
                label = { Text(entry.value.toString()) },
                leadingIcon = { Text(entry.key) },
            )
        }
        if(enableAdd){
            AssistChip(
                onClick = { /*TODO*/ },
                label = { Icon(Icons.Filled.Add, contentDescription = "Add new reaction") },
            )
        }
    }
}