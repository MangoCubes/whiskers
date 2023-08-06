package ch.skew.whiskers.misskey.data.api

import ch.skew.whiskers.misskey.data.Note
import kotlinx.serialization.Serializable

@Serializable
data class NotesTimelineReqData(
    val i: String,
    val limit: Int? = 10,
    val sinceId: String? = null,
    val untilId: String? = null,
    val sinceDate: Int? = null,
    val untilDate: Int? = null,
    val includeMyRenotes: Boolean? = true,
    val includeRenotedMyNotes: Boolean? = true,
    val includeLocalRenotes: Boolean? = true,
    val withFiles: Boolean? = false,
    val withReplies: Boolean
)

typealias NotesTimelineResData = List<Note>