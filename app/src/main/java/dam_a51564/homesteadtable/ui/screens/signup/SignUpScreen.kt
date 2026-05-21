package dam_a51564.homesteadtable.ui.screens.signup

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

@Composable
fun SignUpScreen(signUpViewModel: SignUpViewModel, onNavigateBack: () -> Unit) {
    val signUpUIState by signUpViewModel.uiState.collectAsState()

    LaunchedEffect(signUpUIState.isSignUpSuccessful) {
        if (signUpUIState.isSignUpSuccessful) {
            onNavigateBack()
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

        Text(
            text = stringResource(R.string.create_account),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.signup_info),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(stringResource(R.string.username_field), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = signUpUIState.fullName,
            onValueChange = { signUpViewModel.onUsernameChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.enter_your_username), color = MaterialTheme.colorScheme.onSurfaceVariant) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = Terracotta,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.email_field), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = signUpUIState.email,
            onValueChange = { signUpViewModel.onEmailChange(it) },
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
            value = signUpUIState.password,
            onValueChange = { signUpViewModel.onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = Terracotta,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.confirm_password), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = signUpUIState.confirmPassword,
            onValueChange = { signUpViewModel.onConfirmPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = Terracotta,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = { signUpViewModel.onSignUpClick() },
            enabled = !signUpUIState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
            shape = MaterialTheme.shapes.medium
        ) {
            if (signUpUIState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(stringResource(R.string.btn1_sign_up), style = MaterialTheme.typography.labelLarge)
            }
        }

        if (signUpUIState.errorMessage != null) {
            Text(
                text = signUpUIState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.already_have_an_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextButton(onClick = onNavigateBack) {
                Text(stringResource(R.string.btn1_log_in), color = Terracotta, fontWeight = FontWeight.Bold)
            }
        }
    }
}