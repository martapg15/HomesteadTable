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

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState()) // Backing property to avoid state updates from other classes
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newUsername: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newUsername, errorMessage = null)
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(password = newPassword, errorMessage = null)
        }
    }

    fun onRememberMeChange(isChecked: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(rememberMe = isChecked)
        }
    }

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