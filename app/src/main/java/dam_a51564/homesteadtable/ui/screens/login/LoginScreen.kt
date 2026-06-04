package dam_a51564.homesteadtable.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.R
import dam_a51564.homesteadtable.ui.theme.*

/**
 * Displays the login screen for the application.
 *
 * Provides input fields for email and password authentication, a "Remember Me" toggle, and options to
 * navigate to the password reset or user registration screens. Triggers a navigation callback upon successful login.
 *
 * @param loginViewModel The [LoginViewModel] managing authentication state, input validation, and login actions.
 * @param onNavigateToSignUp Callback invoked to navigate to the user registration screen.
 * @param onLoginSuccess Callback invoked automatically when the login state registers as successful.
 * @param onNavigateToForgotPassword Callback invoked to navigate to the password reset screen.
 */
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val loginUIState by loginViewModel.uiState.collectAsState()

    // Observe state changes: When isLoggedIn is true, trigger the navigation
    LaunchedEffect(loginUIState.isLoggedIn) {
        if (loginUIState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Greeting Section
        Text(
            text = stringResource(R.string.hello),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.app_intro),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(stringResource(R.string.email_field), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = loginUIState.email,
            onValueChange = { loginViewModel.onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            placeholder = { Text(stringResource(R.string.enter_your_email), color = MaterialTheme.colorScheme.onSurfaceVariant) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = Terracotta,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.password_field), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = loginUIState.password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            placeholder = { Text(stringResource(R.string.enter_your_password), color = MaterialTheme.colorScheme.onSurfaceVariant) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = Terracotta,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        // Remember Me and Forgot Password Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = loginUIState.rememberMe,
                    onCheckedChange = { loginViewModel.onRememberMeChange(it) },
                    colors = CheckboxDefaults.colors(checkedColor = Terracotta)
                )
                Text(stringResource(R.string.btn_remember_me), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
            TextButton(onClick = onNavigateToForgotPassword) {
                Text(stringResource(R.string.btn_forgot_password), color = BurntRed, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = { loginViewModel.onLoginClick() },
            enabled = !loginUIState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
            shape = MaterialTheme.shapes.medium
        ) {
            if (loginUIState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(stringResource(R.string.btn_log_in), style = MaterialTheme.typography.labelLarge)
            }
        }

        // Sign Up Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.dont_have_an_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextButton(onClick = onNavigateToSignUp) {
                Text(stringResource(R.string.btn_sign_up), color = Terracotta, fontWeight = FontWeight.Bold)
            }
        }

        if (loginUIState.errorMessage != null) {
            Text(
                text = loginUIState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}