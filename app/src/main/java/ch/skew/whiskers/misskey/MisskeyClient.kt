package ch.skew.whiskers.misskey

import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.api.AccountIResData
import ch.skew.whiskers.misskey.data.api.CreateReactionReqData
import ch.skew.whiskers.misskey.data.api.CreateReactionResData
import ch.skew.whiskers.misskey.data.api.DeleteReactionReqData
import ch.skew.whiskers.misskey.data.api.DeleteReactionResData
import ch.skew.whiskers.misskey.data.api.EmojisResData
import ch.skew.whiskers.misskey.data.api.GetReactionsReqData
import ch.skew.whiskers.misskey.data.api.GetReactionsResData
import ch.skew.whiskers.misskey.data.api.NotesTimelineReqData


class MisskeyClient(
    private val api: MisskeyAPI,
    val username: String,
    val id: Int
) {
    suspend fun deleteReaction(noteId: String): Result<DeleteReactionResData> {
        return this.api.deleteReaction(DeleteReactionReqData(noteId))
    }
    suspend fun getReactions(noteId: String, type: String? = null, limit: Int? = 10, offset: Int? = 0, sinceId: String? = null, untilId: String? = null): Result<GetReactionsResData> {
        return this.api.getReactions(GetReactionsReqData(noteId, type, limit, offset, sinceId, untilId))
    }
    suspend fun createReaction(reaction: String, note: String): Result<CreateReactionResData> {
        return this.api.createReaction(CreateReactionReqData(note, reaction))
    }
    suspend fun getDetailedUserData(): Result<AccountIResData> {
        return this.api.accountI()
    }

    suspend fun getEmojis(): Result<EmojisResData> {
        return this.api.metaEmojis()
    }
    suspend fun getNotesTimeline(
        limit: Int = 10,
        sinceId: String? = null,
        untilId: String? = null,
        sinceDate: Int? = null,
        untilDate: Int? = null,
        includeMyRenotes: Boolean = true,
        includeRenotedMyNotes: Boolean = true,
        includeLocalRenotes: Boolean = true,
        withFiles: Boolean = false,
        withReplies: Boolean? = null
    ): Result<List<Note>> {
        val body = NotesTimelineReqData(
            limit,
            sinceId,
            untilId,
            sinceDate,
            untilDate,
            includeMyRenotes,
            includeRenotedMyNotes,
            includeLocalRenotes,
            withFiles,
            withReplies ?: false
        )
        return this.api.notesTimeline(body)
    }

    companion object {
        fun from(data: AccountData): MisskeyClient {
            return MisskeyClient(
                MisskeyAPI(data.host, data.accessToken),
                data.username,
                data.id
            )
        }
    }
}