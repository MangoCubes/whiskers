package ch.skew.whiskers.misskey.data.api

import kotlinx.serialization.Serializable

@Serializable
enum class OnlineStatus(val status: String) {
    Unknown("unknown"),
    Online("online"),
    Active("active"),
    Offline("offline")
}

@Serializable
data class User(
    val id: String,
    val name: String? = null,
    val username: String,
    val host: String? = null,
    val avatarUrl: String? = null,
    val avatarBlurhash: String? = null,
    val isAdmin: Boolean? = false,
    val isModerator: Boolean? = false,
    val isBot: Boolean? = null,
    val isCat: Boolean? = null,
    val onlineStatus: OnlineStatus? = null
)