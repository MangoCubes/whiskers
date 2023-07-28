package ch.skew.whiskers.misskey.data

import kotlinx.serialization.Serializable

@Serializable
data class DriveFileProperties(
    val width: Int? = null,
    val height: Int? = null,
    val orientation: Int? = null,
    val avgColor: String? = null
)

@Serializable
data class DriveFile(
    val id: String,
    val createdAt: String,
    val name: String,
    val type: String,
    val md5: String,
    val size: Int,
    val isSensitive: Boolean,
    val blurhash: String? = null,
    val properties: DriveFileProperties,
    val url: String? = null,
    val thumbnailUrl: String? = null,
    val comment: String? = null,
    val folderId: String? = null,
    val folder: DriveFolder? = null,
    val userId: String? = null,
    val user: User? = null
)