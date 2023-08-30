package ch.skew.whiskers.data.settings

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NsfwMedia {
    Blur,
    Hide,
    Show
}
@Entity(
    tableName = "settings"
)
data class Settings(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    /**
     * True -> Thumbnail is used even for single image
     * False -> Single image is displayed as full image
     */
    val useThumbnailForSingleImage: Boolean = true,
    /**
     * True -> All images are treated as NSFW by default
     */
    val hideAllImagesByDefault: Boolean = false,
    /**
     * How to show NSFW media
     * Blur -> Show blurred image
     * Hide -> Completely hide image
     * Show -> Show image completely
     */
    val nsfwMedia: NsfwMedia = NsfwMedia.Hide,
    val hideFullNsfwMedia: Boolean = true
)