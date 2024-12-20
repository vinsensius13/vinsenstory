package com.example.vinsenstory.addstory

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinsenstory.data.repository.StoryRepository
import com.example.vinsenstory.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _addStoryState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addStoryState: StateFlow<UiState<Unit>> = _addStoryState

    fun uploadStory(description: String, imageUri: Uri?) {
        if (imageUri == null) {
            _addStoryState.value = UiState.Error("Please select an image")
            return
        }

        viewModelScope.launch {
            _addStoryState.value = UiState.Loading
            try {
                val filePart = uriToMultipart(imageUri)
                storyRepository.uploadStory(description, filePart)
                _addStoryState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _addStoryState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    private fun uriToMultipart(uri: Uri): MultipartBody.Part {
        val file = File(uri.path ?: "")
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }
}
