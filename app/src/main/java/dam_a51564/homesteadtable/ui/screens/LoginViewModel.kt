package dam_a51564.homesteadtable.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState()) // Backing property to avoid state updates from other classes
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _uiState.update { currentState ->
            currentState.copy(username = newUsername, errorMessage = null)
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
        // Example of how state changes during an async operation
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // TODO: In Phase 2, this is where AuthRepository will be called to check Firebase.
        // For now, we simulate a loading state and then a success.
        // Simulating an async Firebase call
        viewModelScope.launch {
            delay(1000) // Simulate network delay
            _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
        }
    }
}