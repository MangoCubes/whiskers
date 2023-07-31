package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
data class CommonErrorData(
    val code: String,
    val message: String,
    val id: String
)