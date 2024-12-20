package com.example.vinsenstory.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vinsenstory.utils.UiState

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val registerState = authViewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineSmall, // Material3 Typography
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        // Password Validation Message
        if (password.length < 8) {
            Text(
                text = "Password harus minimal 8 karakter!",
                color = MaterialTheme.colorScheme.error, // Material3 ColorScheme
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Register Button
        Button(
            onClick = { authViewModel.register(name, email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Register")
        }

        // Register State Handling
        when (val state = registerState.value) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            is UiState.Success -> {
                Text(
                    text = "Registration successful!",
                    color = MaterialTheme.colorScheme.primary, // Material3 ColorScheme
                    modifier = Modifier.padding(top = 16.dp)
                )
                LaunchedEffect(Unit) {
                    onRegisterSuccess()
                }
            }
            is UiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error, // Material3 ColorScheme
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            else -> {}
        }
    }
}
