package ch.skew.whiskers.misskey.data

import kotlinx.serialization.Serializable

@Serializable
data class DriveFolder(
    val id: String,
    val createdAt: String,
    val name: String,
    val foldersCount: Int? = null,
    val filesCount: Int? = null,
    val parentId: String? = null,
    val parent: DriveFolder? = null
)