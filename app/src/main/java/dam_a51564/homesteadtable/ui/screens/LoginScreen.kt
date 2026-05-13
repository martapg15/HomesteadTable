package dam_a51564.homesteadtable.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.ui.theme.*

@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {
    val loginUIState by loginViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Cream
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Greeting Section
        Text(
            text = "Hello,",
            style = MaterialTheme.typography.displayMedium, // ExtraBold Espresso
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Let's Cook Something Amazing",
            style = MaterialTheme.typography.bodyLarge,
            color = WarmTan,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Input Fields
        Text("Username", style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = loginUIState.username,
            onValueChange = { loginViewModel.onUsernameChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your username", color = LightTan) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = ParchmentBorder,
                focusedBorderColor = Terracotta
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Password", style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = loginUIState.password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            placeholder = { Text("Enter your password", color = LightTan) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = ParchmentBorder,
                focusedBorderColor = Terracotta
            )
        )

        // Remember Me & Forgot Password Row
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
                Text("Remember me", style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = { /* TODO */ }) {
                Text("Forgot Password!", color = BurntRed, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = { loginViewModel.onLoginClick() },
            enabled = !loginUIState.isLoading, // Disable button while loading
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
            shape = MaterialTheme.shapes.medium
        ) {
            if (loginUIState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("LOG IN", style = MaterialTheme.typography.labelLarge)
            }
        }

        // Sign Up Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Don't have an Account? ",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextButton(onClick = { /* TODO */ }) {
                Text("Sign Up", color = Terracotta, fontWeight = FontWeight.Bold)
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