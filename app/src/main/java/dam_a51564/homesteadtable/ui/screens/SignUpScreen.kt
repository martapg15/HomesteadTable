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
fun SignUpScreen(signUpViewModel: SignUpViewModel, onNavigateBack: () -> Unit) {
    val signUpUIState by signUpViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Cream
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Create Account,",
            style = MaterialTheme.typography.displayMedium, // ExtraBold Espresso
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Join the Homestead Table",
            style = MaterialTheme.typography.bodyLarge,
            color = WarmTan, //
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Username Field
        Text("Username", style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = signUpUIState.fullName,
            onValueChange = { signUpViewModel.onUsernameChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter username", color = LightTan) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = ParchmentBorder,
                focusedBorderColor = Terracotta
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        Text("Email", style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = signUpUIState.email,
            onValueChange = { signUpViewModel.onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            placeholder = { Text("Enter email", color = LightTan) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = ParchmentBorder,
                focusedBorderColor = Terracotta
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Fields
        Text("Password", style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = signUpUIState.password,
            onValueChange = { signUpViewModel.onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = ParchmentBorder,
                focusedBorderColor = Terracotta
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Confirm Password", style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = signUpUIState.confirmPassword,
            onValueChange = { signUpViewModel.onConfirmPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = ParchmentBorder,
                focusedBorderColor = Terracotta
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = { signUpViewModel.onSignUpClick() },
            enabled = !signUpUIState.isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
            shape = MaterialTheme.shapes.medium
        ) {
            if (signUpUIState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("SIGN UP", style = MaterialTheme.typography.labelLarge)
            }
        }

        if (signUpUIState.errorMessage != null) {
            Text(
                text = signUpUIState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Already have an Account? ",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextButton(onClick = onNavigateBack) {
                Text("Log In", color = Terracotta, fontWeight = FontWeight.Bold)
            }
        }
    }
}