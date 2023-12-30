package ch.skew.whiskers.misskey.data.api

import ch.skew.whiskers.misskey.data.UserLite
import kotlinx.serialization.Serializable


@Serializable
data class GetReactionsReqData(
    val noteId: String,
    val type: String? = null,
    val limit: Int? = 10,
    val offset: Int? = 0,
    val sinceId: String? = null,
    val untilId: String? = null
)

@Serializable
data class ReactionInfo(
    val id: String,
    val createdAt: String,
    val user: UserLite,
    val type: String
)

typealias GetReactionsResData = List<ReactionInfo>

@Serializable
data class CreateReactionReqData(
    val noteId: String,
    val reaction: String
)

typealias CreateReactionResData = Unit

@Serializable
data class DeleteReactionReqData(
    val noteId: String
)

typealias DeleteReactionResData = Unit