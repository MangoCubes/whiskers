package ch.skew.whiskers.misskey

import android.net.Uri
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.api.AccountIResData
import ch.skew.whiskers.misskey.data.api.NotesTimelineReqData


class MisskeyClient(
    private val accessToken: String,
    private val api: MisskeyAPI
) {
    suspend fun getDetailedUserData(): Result<AccountIResData> {
        return this.api.accountI()
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
        val body = NotesTimelineReqData(accessToken, limit, sinceId, untilId, sinceDate, untilDate, includeMyRenotes, includeRenotedMyNotes, includeLocalRenotes, withFiles, withReplies)
        return this.api.notesTimeline(body)
    }

    companion object {
        fun from(data: AccountData): MisskeyClient? {
            return if(data.url === null || data.accessToken === null) null
            else MisskeyClient(
                data.accessToken,
                MisskeyAPI(
                    Uri.parse(data.url)
                )
            )
        }
    }
}