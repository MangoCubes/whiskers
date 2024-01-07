package ch.skew.whiskers.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class AvailableReactions {
    NonSensitive,
    LikeOnly,
    Any,
    None
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReactionChips(
    // List of custom reactions available
    reactions: Map<String, Int>,
    // Max height of the chips in dp
    maxHeight: Int,
    // Whether the chips should be expanded
    expanded: Boolean,
    // Reactions that can be added to this note
    availableReactions: AvailableReactions,
    // Callback to toggle a reaction
    toggleReaction: (String) -> Unit,
    // Reaction that is currently being added
    loadingReaction: String?,
    // Reaction that the user has added
    myReaction: String?,
    // Callback to open the reaction selector
    openSelector: () -> Unit
) {
    FlowRow(
        if (expanded) Modifier.heightIn(max = maxHeight.dp)
        else Modifier
    ) {
        reactions.filter { it.value != 0 }.map { entry ->
            FilterChip(
                onClick = { toggleReaction(entry.key) },
                label = { Text(entry.value.toString()) },
                leadingIcon = { Text(entry.key) },
                enabled = loadingReaction != entry.key,
                selected = myReaction == entry.key
            )
        }
        if (myReaction == null) when(availableReactions){
            AvailableReactions.LikeOnly -> {
                AssistChip(
                    onClick = { toggleReaction("❤") },
                    label = { Text("+") },
                    leadingIcon = { Text("❤") }
                )
            }
            AvailableReactions.None -> {}
            else -> {
                AssistChip(
                    onClick = openSelector,
                    label = { Text("+") },
                )
            }
        }
    }
}