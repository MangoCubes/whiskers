package ch.skew.whiskers.misskey.data

class AuthError: Throwable()

data class AppCreateReq(
    val name: String,
    val description: String,
    val permission: List<String>,
    val callbackUrl: String? = null
)

data class AppCreateRes(
    val id: String,
    val name: String,
    val description: String,
    val permission: List<String>,
    val callbackUrl: String,
    val secret: String?,
    val isAuthorized: Boolean
)