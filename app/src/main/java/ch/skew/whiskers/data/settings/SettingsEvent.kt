package ch.skew.whiskers.data.settings

sealed interface SettingsEvent {
    data class AddSettings(val settings: Settings): SettingsEvent
    data class UpdateSettings(val settings: Settings): SettingsEvent
    data class DeleteSettings(val id: Int): SettingsEvent
}