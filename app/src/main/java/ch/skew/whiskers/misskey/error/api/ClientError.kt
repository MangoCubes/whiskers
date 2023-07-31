package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class ClientErrorData(
    val error: CommonErrorData
)

class ClientError(
    val data: ClientErrorData
): Throwable()