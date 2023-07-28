package ch.skew.whiskers.misskey.data

import kotlinx.serialization.Serializable

@Serializable
data class AuthSessionGenerateReqData(
    val appSecret: String
): ReqData()

@Serializable
data class AuthSessionGenerateResData(
    val token: String,
    val url: String
): ResData()