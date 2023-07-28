package ch.skew.whiskers.misskey.data.api

sealed class ReqData
sealed class ResData

object PingReqData : ReqData()
object PingResData : ResData()