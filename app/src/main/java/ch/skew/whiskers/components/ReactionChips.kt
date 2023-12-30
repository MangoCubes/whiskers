package ch.skew.whiskers.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReactionChips(
    reactions: Map<String, Int>,
    maxHeight: Int,
    expanded: Boolean,
    enableAdd: Boolean,
    toggleReaction: (String) -> Unit,
    loadingReaction: String?,
    myReaction: String?
) {
    FlowRow(
        if (expanded) Modifier.heightIn(max = maxHeight.dp)
        else Modifier
    ) {
        reactions.map { entry ->
            FilterChip(
                onClick = { toggleReaction(entry.key) },
                label = { Text(entry.value.toString()) },
                leadingIcon = { Text(entry.key) },
                enabled = loadingReaction != entry.key,
                selected = myReaction == entry.key
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