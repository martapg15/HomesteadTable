package dam_a51564.homesteadtable.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dam_a51564.homesteadtable.ui.screens.AddRecipeScreen
import dam_a51564.homesteadtable.ui.screens.AddRecipeViewModel
import dam_a51564.homesteadtable.ui.screens.FavouritesScreen
import dam_a51564.homesteadtable.ui.screens.FavouritesViewModel
import dam_a51564.homesteadtable.ui.screens.HomeScreen
import dam_a51564.homesteadtable.ui.screens.HomeViewModel
import dam_a51564.homesteadtable.ui.screens.LoginScreen
import dam_a51564.homesteadtable.ui.screens.LoginViewModel
import dam_a51564.homesteadtable.ui.screens.ProfileScreen
import dam_a51564.homesteadtable.ui.screens.ProfileViewModel
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
                },
                // Navigate to home and clear backstack so user can't go back to login
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
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

        // Home Screen Route
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(
                homeViewModel = homeViewModel,
                onNavigateToFavourites = {
                    navController.navigate("favourites") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToAddRecipe = {
                    navController.navigate("add_recipe")
                }
            )
        }

        // Favourites Screen Route
        composable("favourites") {
            val favouritesViewModel: FavouritesViewModel = viewModel()
            FavouritesScreen(
                favouritesViewModel = favouritesViewModel,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Profile Screen Route
        composable("profile") {
            val profileViewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                profileViewModel = profileViewModel,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToFavourites = {
                    navController.navigate("favourites") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onLogOutSuccess = {
                    // Navigate to login and clear everything up to that point
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Add recipe screen route
        composable("add_recipe") {
            val addRecipeViewModel: AddRecipeViewModel = viewModel()
            AddRecipeScreen(
                addRecipeViewModel = addRecipeViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}