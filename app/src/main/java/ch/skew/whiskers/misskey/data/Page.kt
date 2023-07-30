package ch.skew.whiskers.misskey.data

import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val title: String,
    val name: String,
    val summary: String? = null,
//    val content: List<???>
//    val variables: List<???>
    val userId: String,
    val user: UserLite
)