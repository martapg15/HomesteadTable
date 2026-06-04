package dam_a51564.homesteadtable.ui.screens.profile

/**
 * UI state representing the current user information displayed on the Profile screen.
 *
 * @property userName The display name of the authenticated user.
 * @property email The email address linked to the authenticated account.
 * @property isLoggingOut True when a sign-out request is actively being processed.
 */
data class ProfileUiState(
    val userName: String = "",
    val email: String = "",
    val isLoggingOut: Boolean = false
)
