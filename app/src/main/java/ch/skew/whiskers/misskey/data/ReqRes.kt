package ch.skew.whiskers.misskey.data

sealed class ReqData
sealed class ResData

object PingReqData : ReqData()
object PingResData : ResData()