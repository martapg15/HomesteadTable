package dam_a51564.homesteadtable.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dam_a51564.homesteadtable.ui.screens.recipe_management.AddRecipeScreen
import dam_a51564.homesteadtable.ui.screens.recipe_management.AddRecipeViewModel
import dam_a51564.homesteadtable.ui.screens.recipe_management.EditRecipeScreen
import dam_a51564.homesteadtable.ui.screens.recipe_management.EditRecipeViewModel
import dam_a51564.homesteadtable.ui.screens.favourites.FavouritesScreen
import dam_a51564.homesteadtable.ui.screens.favourites.FavouritesViewModel
import dam_a51564.homesteadtable.ui.screens.home.HomeScreen
import dam_a51564.homesteadtable.ui.screens.home.HomeViewModel
import dam_a51564.homesteadtable.ui.screens.login.LoginScreen
import dam_a51564.homesteadtable.ui.screens.login.LoginViewModel
import dam_a51564.homesteadtable.ui.screens.profile.ProfileScreen
import dam_a51564.homesteadtable.ui.screens.profile.ProfileViewModel
import dam_a51564.homesteadtable.ui.screens.detail.RecipeDetailScreen
import dam_a51564.homesteadtable.ui.screens.detail.RecipeDetailViewModel
import dam_a51564.homesteadtable.ui.screens.signup.SignUpScreen
import dam_a51564.homesteadtable.ui.screens.signup.SignUpViewModel

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
                },
                onNavigateToRecipeDetail = { recipeId ->
                    navController.navigate("recipe_detail/$recipeId")
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
                },
                onNavigateToRecipeDetail = {
                    navController.navigate("recipe_detail/${it}")
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

        // Recipe detail screen route
        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            val recipeDetailViewModel: RecipeDetailViewModel = viewModel()

            recipeDetailViewModel.loadRecipe(recipeId)

            RecipeDetailScreen(
                viewModel = recipeDetailViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { recipeId ->
                    navController.navigate("edit_recipe/$recipeId")
                }
            )
        }

        composable("edit_recipe/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            val editRecipeViewModel: EditRecipeViewModel = viewModel()

            editRecipeViewModel.loadRecipe(recipeId)

            EditRecipeScreen(
                editRecipeViewModel = editRecipeViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}