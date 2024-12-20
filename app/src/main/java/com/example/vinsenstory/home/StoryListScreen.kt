package com.example.vinsenstory.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vinsenstory.data.model.Story

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryListScreen(
    stories: List<Story>,
    onStoryClick: (Story) -> Unit,
    onAddStoryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Story List") },
                actions = {
                    IconButton(onClick = onAddStoryClick) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Story")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stories) { story ->
                StoryListItem( // Gunakan nama fungsi unik
                    story = story,
                    onClick = { onStoryClick(story) }
                )
            }
        }
    }
}

@Composable
fun StoryListItem( // Ubah nama fungsi agar unik
    story: Story,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Material 3 Elevation
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Story Image
            AsyncImage(
                model = story.photoUrl,
                contentDescription = "Story Image",
                modifier = Modifier
                    .size(64.dp)
                    .aspectRatio(1f)
            )

            // Story Name
            Text(
                text = story.name,
                style = MaterialTheme.typography.bodyLarge, // Material 3 Typography
                modifier = Modifier.weight(1f)
            )
        }
    }
}
