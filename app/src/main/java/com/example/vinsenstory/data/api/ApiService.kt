package com.example.vinsenstory.data.api

import com.example.vinsenstory.data.model.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    // Register user
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    // Login user
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Get stories
    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1, // Default page 1
        @Query("size") size: Int = 10, // Default size 10
        @Query("location") location: Int = 0 // Default location 0 (disabled)
    ): StoriesResponse

    // Upload a story
    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String, // Authorization token
        @Part("description") description: String,
        @Part file: MultipartBody.Part
    ): UploadResponse

    // Get story detail
    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") token: String, // Authorization token
        @Path("id") storyId: String // Story ID
    ): StoryDetailResponse
}
