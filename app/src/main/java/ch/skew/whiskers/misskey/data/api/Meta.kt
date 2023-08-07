package ch.skew.whiskers.misskey.data.api

import kotlinx.serialization.Serializable

@Serializable
data class Emoji (
    val aliases: List<String>,
    val name: String,
    val category: String? = null,
    val url: String,
    val isSensitive: Boolean? = null,
    val roleIdsThatCanBeUsedThisEmojiAsReaction: List<String>? = null
)
@Serializable
object EmojisReqData

@Serializable
data class EmojisResData(
    val emojis: List<Emoji>
)