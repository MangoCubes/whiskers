package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class InternalServerErrorData(
    val error: CommonErrorData
)

class InternalServerError(
    val data: InternalServerErrorData
): Throwable()