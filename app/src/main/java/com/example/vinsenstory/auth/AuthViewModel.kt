package com.example.vinsenstory.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinsenstory.data.local.TokenManager
import com.example.vinsenstory.data.model.LoginResponse
import com.example.vinsenstory.data.model.RegisterResponse
import com.example.vinsenstory.data.repository.AuthRepository
import com.example.vinsenstory.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState

    private val _registerState = MutableStateFlow<UiState<RegisterResponse>>(UiState.Idle)
    val registerState: StateFlow<UiState<RegisterResponse>> = _registerState

    // Observe authToken for UI state management
    val authToken = tokenManager.authToken

    /**
     * Login function
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            try {
                val result = authRepository.login(email, password)
                // Save token to TokenManager after successful login
                result.loginResult?.token?.let { token ->
                    tokenManager.saveAuthToken(token)
                }
                _loginState.value = UiState.Success(result)
            } catch (e: Exception) {
                _loginState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    /**
     * Register function
     */
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = UiState.Loading
            try {
                val result = authRepository.register(name, email, password)
                _registerState.value = UiState.Success(result)
            } catch (e: Exception) {
                _registerState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    /**
     * Clear the token (Logout)
     */
    fun clearAuthToken() {
        viewModelScope.launch {
            tokenManager.clearAuthToken()
        }
    }
}
