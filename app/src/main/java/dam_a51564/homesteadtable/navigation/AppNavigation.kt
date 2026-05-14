package dam_a51564.homesteadtable.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dam_a51564.homesteadtable.ui.screens.LoginScreen
import dam_a51564.homesteadtable.ui.screens.LoginViewModel
import dam_a51564.homesteadtable.ui.screens.SignUpScreen
import dam_a51564.homesteadtable.ui.screens.SignUpViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login" // The app starts here
    ) {
        // Login Screen Route
        composable("login") {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                loginViewModel = loginViewModel,
                onNavigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        // Sign Up Screen Route
        composable("signup") {
            val signUpViewModel: SignUpViewModel = viewModel()
            SignUpScreen(
                signUpViewModel = signUpViewModel,
                onNavigateBack = {
                    navController.popBackStack() // Goes back to Login
                }
            )
        }
    }
}