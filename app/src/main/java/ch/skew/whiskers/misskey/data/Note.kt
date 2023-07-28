package ch.skew.whiskers.misskey.data

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String,
    val createdAt: String,
    val deletedAt: String? = null,
    val text: String? = null,
    val cw: String? = null,
    val userId: String? = null,
    val user: User,
    val replyId: String? = null,
    val renoteId: String? = null,
    val reply: Note? = null,
    val renote: Note? = null,
    val isHidden: Boolean? = null,
    val visibility: String? = null,
    val mentions: List<String>? = null,
    val visibleUserIds: List<String>? = null,
    val fieldIds: List<String>? = null,
    val files: List<DriveFile>? = null,
    val tags: List<String>? = null,
    // val poll: Poll? = null,
    val channelId: String? = null,
    // val channel: Channel? = null,
    val localOnly: Boolean? = null,
    val reactionAcceptance: String? = null,
    // val reactions: Reaction,
    val renoteCount: Int,
    val repliesCount: Int,
    val uri: String? = null,
    val url: String? = null,
    // val myReaction: Reaction? = null
)