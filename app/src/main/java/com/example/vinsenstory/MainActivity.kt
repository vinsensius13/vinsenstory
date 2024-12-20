package com.example.vinsenstory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vinsenstory.addstory.AddStoryScreen
import com.example.vinsenstory.auth.LoginScreen
import com.example.vinsenstory.auth.RegisterScreen
import com.example.vinsenstory.data.local.TokenManager
import com.example.vinsenstory.data.model.Story
import com.example.vinsenstory.home.HomeScreen
import com.example.vinsenstory.home.StoryDetailScreen
import com.example.vinsenstory.ui.theme.VinsenstoryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager // Inject TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan coroutine untuk mengakses authToken
        lifecycleScope.launchWhenCreated {
            val token = tokenManager.authToken.firstOrNull() // Dapatkan nilai token secara sinkron
            setContent {
                VinsenstoryApp(isLoggedIn = token != null)
            }
        }
    }
}

@Composable
fun VinsenstoryApp(isLoggedIn: Boolean) {
    VinsenstoryTheme {
        val navController = rememberNavController()
        NavGraph(navController = navController, startDestination = if (isLoggedIn) "home" else "register")
    }
}


@Composable
fun NavGraph(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {

        // Login Screen
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        // Register Screen
        composable("register") {
            RegisterScreen(onRegisterSuccess = {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            })
        }

        // Home Screen
        composable("home") {
            HomeScreen(
                onStoryClick = { story ->
                    navController.navigate("detail/${story.id}")
                },
                onAddStoryClick = {
                    navController.navigate("add")
                }
            )
        }

        // Story Detail Screen
        composable("detail/{storyId}") { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId")
            if (storyId != null) {
                val dummyStory = Story(
                    id = storyId,
                    name = "Dummy Story",
                    description = "This is a dummy story for testing",
                    photoUrl = "https://via.placeholder.com/150",
                    createdAt = "2024-12-05T00:00:00Z"
                )
                StoryDetailScreen(story = dummyStory) {
                    navController.popBackStack()
                }
            }
        }

        // Add Story Screen
        composable("add") {
            AddStoryScreen(onStoryAdded = {
                navController.navigate("home") {
                    popUpTo("add") { inclusive = true }
                }
            })
        }
    }
}

