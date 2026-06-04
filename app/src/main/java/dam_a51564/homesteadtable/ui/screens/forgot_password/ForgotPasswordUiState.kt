package dam_a51564.homesteadtable.ui.screens.forgot_password

/**
 * UI state representing the inputs and submission status for the Forgot Password screen.
 *
 * @property email The email address currently entered by the user.
 * @property isLoading True when the password reset request is in progress.
 * @property isSuccess True when the password reset email has been successfully sent.
 * @property errorMessage Contextual error message to display upon failed validation or network errors.
 */
data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)