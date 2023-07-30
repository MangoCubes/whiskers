package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class InternalServerErrorData(
    val code: String,
    val message: String,
    val id: String
)

class InternalServerError(
    val data: InternalServerErrorData
): Throwable()