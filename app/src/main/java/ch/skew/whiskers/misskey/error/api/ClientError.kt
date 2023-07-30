package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class ClientErrorData(
    val code: String,
    val message: String,
    val id: String
)

class ClientError(
    val data: ClientErrorData
): Throwable()