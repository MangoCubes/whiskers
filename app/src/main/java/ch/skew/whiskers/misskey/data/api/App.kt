package ch.skew.whiskers.misskey.data.api

import kotlinx.serialization.Serializable

@Serializable
data class AppCreateReqData(
    val name: String,
    val description: String,
    val permission: List<String>,
    val callbackUrl: String? = null
)

@Serializable
data class AppCreateResData(
    val id: String,
    val name: String,
    val description: String? = null,
    val permission: List<String>,
    val callbackUrl: String? = null,
    val secret: String? = null,
    val isAuthorized: Boolean? = null
)