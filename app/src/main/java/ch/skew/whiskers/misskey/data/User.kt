package ch.skew.whiskers.misskey.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OnlineStatus {
    @SerialName("unknown")
    Unknown,
    @SerialName("online")
    Online,
    @SerialName("active")
    Active,
    @SerialName("offline")
    Offline
}

@Serializable
enum class TwoFactorBackupCodes {
    @SerialName("full")
    Full,
    @SerialName("partial")
    Partial,
    @SerialName("none")
    None
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
data class UserDetailedNotMe(
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
    // This does not exist in Firefish.
//    val isLimited: Boolean,
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
    // Despite the docs, this may be null.
    val pinnedPage: Page? = null,
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
)

/**
 * Potential TODO: Apparently Firefish API has some properties that do not exist in Misskey API.
 * Subclasses of UserDetailed may be created, that is inherited by UserDetailed of Misskey and Firefish
 * But why is this even a thing???? Why does Firefish send avatarColor property????
 */
@Serializable
data class UserDetailed(
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
//    val isLimited: Boolean,
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
    val pinnedPage: Page? = null,
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
    val preventAiLearning: Boolean? = null,
    val isExplorable: Boolean,
    val isDeleted: Boolean,
    // Does not exist in Firefish
//    val twoFactorBackupCodes: TwoFactorBackupCodes,
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