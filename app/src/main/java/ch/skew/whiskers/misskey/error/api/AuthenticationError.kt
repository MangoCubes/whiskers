package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationErrorData(
    val code: String,
    val message: String,
    val id: String
)

class AuthenticationError(
    val data: AuthenticationErrorData
): Throwable()