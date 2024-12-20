package com.example.vinsenstory.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    // StateFlow untuk memantau perubahan token secara reaktif
    private val _authToken = MutableStateFlow(sharedPreferences.getString("auth_token", null))
    val authToken: StateFlow<String?> = _authToken.asStateFlow()

    /**
     * Menyimpan token ke SharedPreferences dan memperbarui nilai StateFlow
     * @param token Token yang akan disimpan
     */
    fun saveAuthToken(token: String) {
        sharedPreferences.edit().apply {
            putString("auth_token", token)
            apply()
        }
        _authToken.value = token
    }

    /**
     * Membersihkan token dari SharedPreferences dan memperbarui StateFlow
     */
    fun clearAuthToken() {
        sharedPreferences.edit().apply {
            remove("auth_token")
            apply()
        }
        _authToken.value = null
    }

    /**
     * Mengambil token secara sinkron dari SharedPreferences
     * @return Token saat ini atau null jika tidak ada
     */
    fun getAuthToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }
}
