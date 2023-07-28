package ch.skew.whiskers.misskey.data.api

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