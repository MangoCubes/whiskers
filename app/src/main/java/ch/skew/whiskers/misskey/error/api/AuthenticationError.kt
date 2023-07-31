package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationErrorData(
    val error: CommonErrorData
)

class AuthenticationError(
    val data: AuthenticationErrorData
): Throwable()