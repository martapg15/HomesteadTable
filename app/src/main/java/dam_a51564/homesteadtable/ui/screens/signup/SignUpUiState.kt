package dam_a51564.homesteadtable.ui.screens.signup

/**
 * UI state representing the current visual and data properties of the Sign Up screen.
 *
 * @property fullName The full name currently entered by the user.
 * @property email The email address currently entered by the user.
 * @property password The requested account password.
 * @property confirmPassword The password confirmation to ensure they match.
 * @property isLoading True when a registration request is in progress.
 * @property errorMessage Contextual error message to display upon failed registration or validation.
 * @property isSignUpSuccessful True when the account creation process completes successfully.
 */
data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpSuccessful: Boolean = false
)