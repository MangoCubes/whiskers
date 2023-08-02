package ch.skew.whiskers.misskey.data.api

import ch.skew.whiskers.misskey.data.UserDetailed
import kotlinx.serialization.Serializable

@Serializable
data class AccountIReqData(
    val i: String
)

typealias AccountIResData = UserDetailed