package dam_a51564.homesteadtable.ui.screens.login

/**
 * UI state representing the current visual and data properties of the Login screen.
 *
 * @property email The email address currently entered by the user.
 * @property password The password sequence currently entered by the user.
 * @property rememberMe True if the user wishes to stay logged in after the app closes.
 * @property errorMessage Contextual error message to display upon failed authentication.
 * @property isLoading True when an authentication request is in progress.
 * @property isLoggedIn True when the login process has completed successfully.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)
