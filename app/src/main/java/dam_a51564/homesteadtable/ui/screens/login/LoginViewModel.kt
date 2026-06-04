package dam_a51564.homesteadtable.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.AuthRepository
import dam_a51564.homesteadtable.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state and business logic of the Login screen.
 * Handles user input validation and authenticates credentials via [AuthRepository].
 */
class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState()) // Backing property to avoid state updates from other classes
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Updates the email input state and clears any existing error messages.
     *
     * @param newUsername String sequence of updated accounts.
     */
    fun onEmailChange(newUsername: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newUsername, errorMessage = null)
        }
    }

    /**
     * Updates the password input state and clears any existing error messages.
     *
     * @param newPassword Text definitions specifying updating password variables.
     */
    fun onPasswordChange(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(password = newPassword, errorMessage = null)
        }
    }

    /**
     * Updates the "Remember Me" checkbox state.
     *
     * @param isChecked Evaluation boolean specified by input checkboxes.
     */
    fun onRememberMeChange(isChecked: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(rememberMe = isChecked)
        }
    }

    /**
     * Validates inputs and attempts to log the user in via the repository.
     */
    fun onLoginClick() {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your email and password") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = AuthRepository.login(state.email, state.password)

            result.onSuccess {
                SessionManager.setRememberMe(state.rememberMe)
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Invalid credentials. Please try again.") }
            }
        }
    }
}