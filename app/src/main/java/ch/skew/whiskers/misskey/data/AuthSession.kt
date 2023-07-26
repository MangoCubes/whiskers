package ch.skew.whiskers.misskey.data

data class AuthSessionGenerateReqData(
    val appSecret: String
): ReqData()

data class AuthSessionGenerateResData(
    val token: String,
    val url: String
): ResData()