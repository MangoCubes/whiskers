package ch.skew.whiskers.misskey

import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.api.AccountIResData
import ch.skew.whiskers.misskey.data.api.EmojisResData
import ch.skew.whiskers.misskey.data.api.NotesTimelineReqData


class MisskeyClient(
    private val accessToken: String,
    private val api: MisskeyAPI,
    val username: String,
    val id: Int
) {
    suspend fun getDetailedUserData(): Result<AccountIResData> {
        return this.api.accountI(accessToken)
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
            accessToken,
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
                data.accessToken,
                MisskeyAPI(data.host),
                data.username,
                data.id
            )
        }
    }
}