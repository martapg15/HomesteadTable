package dam_a51564.homesteadtable.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(fullName = username, errorMessage = null) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, errorMessage = null) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, errorMessage = null) }
    }

    fun onConfirmPasswordChange(newConfirm: String) {
        _uiState.update { it.copy(confirmPassword = newConfirm, errorMessage = null) }
    }

    fun onSignUpClick() {
        val state = _uiState.value

        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Passwords do not match") }
            return
        }

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // Launch a coroutine to talk to Firebase via the Repository
        viewModelScope.launch {
            val result = AuthRepository.signUp(state.email, state.password, state.fullName)

            result.onSuccess {
                // It worked: Update UI to trigger navigation
                _uiState.update { it.copy(isLoading = false, isSignUpSuccessful = true) }
            }.onFailure { exception ->
                // It failed: Show Firebase's error message to the user
                _uiState.update { it.copy(isLoading = false, errorMessage = exception.localizedMessage) }
            }
        }
    }
}