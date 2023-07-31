package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class ForbiddenErrorData(
    val error: CommonErrorData
)

class ForbiddenError(
    val data: ForbiddenErrorData
): Throwable()