package ch.skew.whiskers.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ch.skew.whiskers.misskey.data.DriveFile
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.roundToInt

// Let me know if a component that does this already exists and I am just reinventing the wheel
// because I couldn't find one.

data class GalleryContent(
    val items: List<DriveFile>,
    val current: Int
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun GalleryViewer(media: GalleryContent, close: () -> Unit) {
    val showUi = remember { mutableStateOf(true) }
    val height = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val interactionSource = remember { MutableInteractionSource() }
    val swipeState = rememberSwipeableState(0) {
        if(it != 0) close()
        return@rememberSwipeableState true
    }
    val anchors = mapOf(0f to 0, height to 1, -height to -1)
    val pagerState = rememberPagerState(
        initialPage = media.current,
        initialPageOffsetFraction = 0f
    )
    Dialog(
        onDismissRequest = close,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = false)
    ){
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            pageCount = media.items.size,
            modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) {
                showUi.value = !showUi.value
            }.swipeable(
                state = swipeState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical
            )
        ) { index ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(media.items[index].url)
                    .crossfade(true)
                    .build(),
                contentDescription = media.items[index].name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
                    .offset { IntOffset(0, swipeState.offset.value.roundToInt()) }

            )
        }
        AnimatedVisibility(
            visible = showUi.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 150)),
            exit = fadeOut(animationSpec = tween(durationMillis = 150))
        ) {
            Row(
                Modifier.background(Color.Black.copy(alpha = 0.5F))
                    .fillMaxWidth()
            ) {
                IconButton(onClick = close) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        "Go back",
                        tint = Color.White
                    )
                }
            }
        }
    }
}