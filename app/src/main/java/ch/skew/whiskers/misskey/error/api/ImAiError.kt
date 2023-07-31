package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class ImAiErrorData(
    val error: CommonErrorData
)

class ImAiError(
    val data: ImAiErrorData
): Throwable()