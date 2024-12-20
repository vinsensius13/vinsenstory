package com.example.vinsenstory.addstory

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.vinsenstory.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStoryScreen(
    onStoryAdded: () -> Unit,
    addStoryViewModel: AddStoryViewModel = hiltViewModel()
) {
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val addStoryState = addStoryViewModel.addStoryState.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Story") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display Image if Selected
                selectedImageUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 16.dp)
                    )
                }

                // Button to Select Image
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pick Image")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description Input
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Upload Button
                Button(
                    onClick = {
                        addStoryViewModel.uploadStory(description, selectedImageUri)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedImageUri != null && description.isNotBlank() // Ensure inputs are valid
                ) {
                    Text("Upload Story")
                }

                // Show Loading or Result
                when (val state = addStoryState.value) {
                    is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                    is UiState.Success -> {
                        Text(
                            text = "Story added successfully!",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        LaunchedEffect(Unit) {
                            onStoryAdded()
                        }
                    }
                    is UiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}
