package ch.skew.whiskers.misskey.data

import kotlinx.serialization.Serializable

@Serializable
enum class OnlineStatus(val status: String) {
    Unknown("unknown"),
    Online("online"),
    Active("active"),
    Offline("offline")
}

@Serializable
enum class TwoFactorBackupCodes(val status: String) {
    Full("full"),
    Partial("partial"),
    None("none")
}

@Serializable
data class UserLite(
    val id: String,
    val name: String? = null,
    val username: String,
    val host: String? = null,
    val avatarUrl: String? = null,
    val avatarBlurhash: String? = null,
    val isAdmin: Boolean? = false,
    val isModerator: Boolean? = false,
    val isBot: Boolean? = null,
    val isCat: Boolean? = null,
    val onlineStatus: OnlineStatus? = null
)

@Serializable
data class UserField(
    val name: String? = null,
    val value: String? = null
)
@Serializable
data class User(
    val id: String,
    val name: String? = null,
    val username: String,
    val host: String? = null,
    val avatarUrl: String? = null,
    val avatarBlurhash: String? = null,
    val isAdmin: Boolean? = false,
    val isModerator: Boolean? = false,
    val isBot: Boolean? = null,
    val isCat: Boolean? = null,
    val onlineStatus: OnlineStatus? = null,
    val url: String? = null,
    val uri: String? = null,
    val movedToUrl: String? = null,
    val alsoKnownAs: List<String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val lastFetchedAt: String? = null,
    val bannerBlurHash: String? = null,
    val isLocked: Boolean,
    val isSilenced: Boolean,
    val isLimited: Boolean,
    val isSuspended: Boolean,
    val description: String? = null,
    val location: String? = null,
    val birthday: String? = null,
    val lang: String? = null,
    val fields: List<UserField>,
    val followersCount: Int,
    val followingCount: Int,
    val notesCount: Int,
    val pinnedNoteIds: List<String>,
    val pinnedNotes: List<Note>,
    val pinnedPageId: String? = null,
    val pinnedPage: Page,
    val publicReactions: Boolean,
    val twoFactorEnabled: Boolean = false,
    val usePasswordLessLogin: Boolean = false,
    val securityKeys: Boolean = false,
    val isFollowing: Boolean? = null,
    val isFollowed: Boolean? = null,
    val hasPendingFollowRequestFromYou: Boolean? = null,
    val hasPendingFollowRequestToYou: Boolean? = null,
    val isBlocking: Boolean? = null,
    val isBlocked: Boolean? = null,
    val isMuted: Boolean? = null,
    val isRenoteMuted: Boolean? = null,
    val memo: String? = null,
    val avatarId: String? = null,
    val bannerId: String? = null,
    val injectFeaturedNote: Boolean? = null,
    val receiveAnnouncementEmail: Boolean? = null,
    val alwaysMarkNsfw: Boolean? = null,
    val autoSensitive: Boolean? = null,
    val carefulBot: Boolean? = null,
    val autoAcceptFollowed: Boolean? = null,
    val noCrawle: Boolean,
    val preventAiLearning: Boolean,
    val isExplorable: Boolean,
    val isDeleted: Boolean,
    val twoFactorBackupCodes: TwoFactorBackupCodes,
    val hideOnlineStatus: Boolean,
    val hasUnreadSpecifiedNotes: Boolean,
    val hasUnreadMentions: Boolean,
    val hasUnreadAnnouncement: Boolean,
    val hasUnreadAntenna: Boolean,
    val hasUnreadNotification: Boolean,
    val hasPendingReceivedFollowRequest: Boolean,
    val mutedWords: List<String>,
    val mutedInstances: List<String>? = null,
    val mutingNotificationTypes: List<String>? = null,
    val emailNotificationTypes: List<String>? = null,
    val email: String? = null,
    val emailVerified: Boolean? = null,
//    val securityKeysList: List<???>? = null
)