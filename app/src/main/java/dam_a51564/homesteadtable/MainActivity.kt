package dam_a51564.homesteadtable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cloudinary.android.MediaManager
import dam_a51564.homesteadtable.data.AuthRepository
import dam_a51564.homesteadtable.data.SessionManager
import dam_a51564.homesteadtable.navigation.AppNavigation
import dam_a51564.homesteadtable.ui.theme.HomesteadTableTheme

/**
 * The primary entry point of the Android application.
 *
 * Responsibilities include:
 * - Initializing third-party services, such as Cloudinary [MediaManager] for image uploads.
 * - Initializing the local [SessionManager] to handle shared preferences.
 * - Checking the current Firebase Authentication state to determine the starting screen.
 * - Handling the "Remember Me" session logic (automatically logging out users who opted not to be remembered upon closing the app).
 * - Setting up the global Compose UI theme and triggering the [AppNavigation] graph.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Cloudinary
        val config = HashMap<String, String>()
        config["cloud_name"] = "ddrgcvxmo"

        MediaManager.init(this, config)
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