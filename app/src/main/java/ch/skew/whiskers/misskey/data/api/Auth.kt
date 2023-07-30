package ch.skew.whiskers.misskey.data.api

import ch.skew.whiskers.misskey.data.UserDetailedNotMe
import kotlinx.serialization.Serializable

@Serializable
data class AuthSessionGenerateReqData(
    val appSecret: String
)

@Serializable
data class AuthSessionGenerateResData(
    val token: String,
    val url: String
)
@Serializable
data class AuthSessionUserkeyReqData(
    val appSecret: String,
    val token: String
)

@Serializable
data class AuthSessionUserkeyResData(
    val accessToken: String,
    val user: UserDetailedNotMe
)