package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class ForbiddenErrorData(
    val code: String,
    val message: String,
    val id: String
)

class ForbiddenError(
    val data: ForbiddenErrorData
): Throwable()