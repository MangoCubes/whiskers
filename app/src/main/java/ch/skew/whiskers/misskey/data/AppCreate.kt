package ch.skew.whiskers.misskey.data

class AuthError: Throwable()

data class AppCreateReqData(
    val name: String,
    val description: String,
    val permission: List<String>,
    val callbackUrl: String? = null
): ReqData()

data class AppCreateResData(
    val id: String,
    val name: String,
    val description: String,
    val permission: List<String>,
    val callbackUrl: String,
    val secret: String?,
    val isAuthorized: Boolean
): ResData()