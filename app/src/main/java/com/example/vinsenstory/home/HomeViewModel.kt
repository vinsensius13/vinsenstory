package com.example.vinsenstory.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinsenstory.data.model.Story
import com.example.vinsenstory.data.repository.StoryRepository
import com.example.vinsenstory.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _storyListState = MutableStateFlow<UiState<List<Story>>>(UiState.Idle)
    val storyListState: StateFlow<UiState<List<Story>>> = _storyListState

    private val _logoutState = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val logoutState: StateFlow<UiState<Boolean>> = _logoutState

    init {
        fetchStories() // Fetch stories on ViewModel initialization
    }

    /**
     * Fetch stories from the repository with default pagination and location
     */
    fun fetchStories(page: Int = 1, size: Int = 10, location: Int = 0) {
        viewModelScope.launch {
            _storyListState.value = UiState.Loading
            try {
                val response = storyRepository.getStories(page, size, location)
                _storyListState.value = UiState.Success(response.listStory) // Extract list of stories
            } catch (e: Exception) {
                _storyListState.value = UiState.Error(e.message ?: "An error occurred while fetching stories")
            }
        }
    }

    /**
     * Clear session and perform logout
     */
    fun logout() {
        viewModelScope.launch {
            try {
                storyRepository.clearSession() // Ensure this function is implemented in `StoryRepository`
                _storyListState.value = UiState.Idle // Reset the state
            } catch (e: Exception) {
                _storyListState.value = UiState.Error(e.message ?: "Failed to logout")
            }
        }
    }
}
