package ch.skew.whiskers.misskey.error

class SessionGenerationError(
    override val cause: Throwable? = null
): Throwable()