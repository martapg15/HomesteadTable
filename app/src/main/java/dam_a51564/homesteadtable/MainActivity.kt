package dam_a51564.homesteadtable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dam_a51564.homesteadtable.data.AuthRepository
import dam_a51564.homesteadtable.data.SessionManager
import dam_a51564.homesteadtable.navigation.AppNavigation
import dam_a51564.homesteadtable.ui.theme.HomesteadTableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SessionManager.init(this)

        // Logic for remember me functionality
        val currentUser = AuthRepository.getCurrentUser()
        if (currentUser != null && !SessionManager.isRememberMe()) {
            // User closed the app previously and didn't want to be remembered
            AuthRepository.logout()
        }

        // Determine where the app should start
        val startingScreen = if (AuthRepository.getCurrentUser() != null) "home" else "login"

        enableEdgeToEdge()
        setContent {
            HomesteadTableTheme {
                HomesteadTableTheme {
                    AppNavigation(appStart = startingScreen)
                }
            }
        }
    }
}