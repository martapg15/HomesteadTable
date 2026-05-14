package dam_a51564.homesteadtable

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dam_a51564.homesteadtable.navigation.AppNavigation
import dam_a51564.homesteadtable.ui.screens.LoginScreen
import dam_a51564.homesteadtable.ui.screens.LoginViewModel
import dam_a51564.homesteadtable.ui.screens.SignUpScreen
import dam_a51564.homesteadtable.ui.screens.SignUpViewModel
import dam_a51564.homesteadtable.ui.theme.HomesteadTableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomesteadTableTheme {
                HomesteadTableTheme {
                    AppNavigation()
                }
            }
        }
    }
}

/*@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    HomesteadTableTheme {
        val previewViewModel = LoginViewModel()
        LoginScreen(loginViewModel = previewViewModel)
    }
}

fun SignUpScreenPreview() {
    HomesteadTableTheme {
        val previewViewModel = SignUpViewModel()
        SignUpScreen(signUpViewModel = previewViewModel)
    }
}*/