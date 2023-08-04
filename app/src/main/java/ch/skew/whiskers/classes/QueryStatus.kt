package ch.skew.whiskers.classes

enum class QueryStatus {
    Querying,
    Success,
    Error
}

sealed class DataQueryStatus<out T> {
    data class Querying(val refresh: Boolean): DataQueryStatus<Nothing>()
    data class Error(val error: Throwable): DataQueryStatus<Nothing>()
    data class Success<T>(val item: T): DataQueryStatus<T>()
}

sealed class ErrorQueryStatus {
    data class Querying(val refresh: Boolean): ErrorQueryStatus()
    data class Error(val error: Throwable): ErrorQueryStatus()
    object Success: ErrorQueryStatus()
}