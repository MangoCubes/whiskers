package ch.skew.whiskers.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun LabelledRadioButtonPreview() {
    Column {
        LabelledRadioButton("Selected", true) {}
        LabelledRadioButton("Not selected", false) {}
        LabelledRadioButton("Loooooooooooooooooooooooooooooooooooooooooooooooong text", true) {}
    }
}

@Composable
fun LabelledRadioButton(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onSelect
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 16.dp)
        )
    }

}