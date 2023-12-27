package ch.skew.whiskers.misskey.data.api

import kotlinx.serialization.Serializable

@Serializable
data class CreateReactionReqData(
    val noteId: String,
    val reaction: String
)

@Serializable
object CreateReactionResData