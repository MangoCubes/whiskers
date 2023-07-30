package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class ImAiErrorData(
    val code: String,
    val message: String,
    val id: String
)

class ImAiError(
    val data: ImAiErrorData
): Throwable()