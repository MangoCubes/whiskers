package ch.skew.whiskers.misskey.error.api

import kotlinx.serialization.Serializable

@Serializable
class InternalServerError(
    val data: CommonErrorData
): Throwable()