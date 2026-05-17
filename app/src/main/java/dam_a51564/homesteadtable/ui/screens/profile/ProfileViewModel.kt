package dam_a51564.homesteadtable.ui.screens.profile

import androidx.lifecycle.ViewModel
import dam_a51564.homesteadtable.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        // Ask Firebase for the currently logged-in user
        val user = AuthRepository.getCurrentUser()

        if (user != null) {
            val userEmail = user.email ?: "No Email Provided"

            // Extract the part before the '@' symbol and capitalize it to use as a display name
            val generatedName = userEmail.substringBefore("@").replaceFirstChar { it.uppercase() }

            _uiState.update {
                it.copy(
                    email = userEmail,
                    userName = user.displayName?.takeIf { name -> name.isNotBlank() } ?: generatedName
                )
            }
        }
    }

    fun onLogOut() {
        // Tell Firebase to destroy the current session
        AuthRepository.logout()
        _uiState.update { it.copy(isLoggingOut = true) }
    }
}