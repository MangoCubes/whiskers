package ch.skew.whiskers.data

import kotlinx.serialization.Serializable

@Serializable
data class WhiskersSettings(
    val useThumbnailForSingleImage: Boolean = true,
    val hideAllImagesByDefault: Boolean = false
)