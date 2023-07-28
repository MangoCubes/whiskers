package ch.skew.whiskers.misskey.data

import kotlinx.serialization.Serializable

class AuthError: Throwable()

@Serializable
data class AppCreateReqData(
    val name: String,
    val description: String,
    val permission: List<String>,
    val callbackUrl: String? = null
): ReqData()

@Serializable
data class AppCreateResData(
    val id: String,
    val name: String,
    val description: String? = null,
    val permission: List<String>,
    val callbackUrl: String? = null,
    val secret: String? = null,
    val isAuthorized: Boolean? = null
): ResData()