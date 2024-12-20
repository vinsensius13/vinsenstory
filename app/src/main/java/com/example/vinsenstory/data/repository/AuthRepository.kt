package com.example.vinsenstory.data.repository

import com.example.vinsenstory.data.api.ApiService
import com.example.vinsenstory.data.model.LoginRequest
import com.example.vinsenstory.data.model.LoginResponse
import com.example.vinsenstory.data.model.RegisterRequest
import com.example.vinsenstory.data.model.RegisterResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(email: String, password: String): LoginResponse {
        val request = LoginRequest(email, password)
        return apiService.login(request)
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        val request = RegisterRequest(name, email, password)
        return apiService.register(request)
    }
}
