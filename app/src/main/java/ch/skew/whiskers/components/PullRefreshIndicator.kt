package ch.skew.whiskers.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshIndicator(
    state: PullRefreshState,
    refreshing: Boolean
) {
    Surface(
        modifier = Modifier
            .size(30.dp)
            .pullRefreshIndicatorTransform(state, false)
            .zIndex(100F),
        shape = CircleShape,
    ) {
        if (!refreshing) {
            Icon(
                if (state.progress > 1F) Icons.Filled.Refresh else Icons.Filled.ArrowDownward,
                "Pull down to refresh",
                modifier = if (state.progress > 1F) Modifier.rotate(state.progress * 180) else Modifier
            )
        } else {
            CircularProgressIndicator(modifier = Modifier.padding(5.dp))
        }
    }
}