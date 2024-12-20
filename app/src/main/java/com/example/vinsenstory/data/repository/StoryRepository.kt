package com.example.vinsenstory.data.repository

import com.example.vinsenstory.data.api.ApiService
import com.example.vinsenstory.data.local.TokenManager
import com.example.vinsenstory.data.model.*
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    /**
     * Get stories from the API with pagination and location filter
     */
    suspend fun getStories(page: Int = 1, size: Int = 10, location: Int = 0): StoriesResponse {
        val token = getAuthToken() ?: throw Exception("Token not found")
        return apiService.getStories(page, size, location)
    }

    /**
     * Upload a new story to the API
     */
    suspend fun uploadStory(description: String, file: MultipartBody.Part): UploadResponse {
        val token = getAuthToken() ?: throw Exception("Token not found")
        return apiService.uploadStory("Bearer $token", description, file)
    }

    /**
     * Get story details by story ID
     */
    suspend fun getStoryDetail(storyId: String): StoryDetailResponse {
        val token = getAuthToken() ?: throw Exception("Token not found")
        return apiService.getStoryDetail("Bearer $token", storyId)
    }

    /**
     * Clear session by removing the saved token
     */
    suspend fun clearSession() {
        tokenManager.clearAuthToken()
    }

    /**
     * Helper function to retrieve the auth token
     */
    private suspend fun getAuthToken(): String? {
        return try {
            tokenManager.authToken.first()
        } catch (e: Exception) {
            null // Return null if token retrieval fails
        }
    }
}
