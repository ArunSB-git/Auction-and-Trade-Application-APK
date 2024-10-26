package com.example.tradeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tradeapplication.ui.theme.TradeApplicationTheme
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import coil.compose.rememberImagePainter


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            TradeApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlayerApp(navController)
                }
            }
        }
    }
}

@Composable
fun PlayerApp(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()
    val viewState by viewModel._playersState

    var currentUserId by remember { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = "login") {
        composable(route = "login") {
            LoginScreen(viewModel) { userid, role ->
                currentUserId=userid
                navController.navigate(Screen.playerScreen.route)
            }
        }
        composable(route = Screen.playerScreen.route) {
            RecipeScreen(
                viewstate = viewState,
                navigationToDetails = { player ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("selectedPlayer", player)
                    navController.currentBackStackEntry?.savedStateHandle?.set("currentUserId", currentUserId) // Pass the user ID
                    navController.navigate(Screen.detailedScreen.route)
                }
            )
        }
        composable(route = Screen.detailedScreen.route) {
            val player = navController.previousBackStackEntry?.savedStateHandle?.get<Player>("selectedPlayer") ?: Player(0, "", "", "", "", 0)
            val userId = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("currentUserId") ?: 0 // Retrieve user ID
            CategoryDetailScreen(category = player, userId = userId, navController = navController)
        }
    }
}

@Composable
fun LoginScreen(viewModel: MainViewModel, onLoginSuccess: (Int, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    if (showToast) {
        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_LONG).show()
        showToast = false // Reset after showing
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(35.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) // Center horizontally)
        {
        //Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_color),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(250.dp) // Adjust size as needed
                    .clip(CircleShape) // Make it oval
                    .padding(bottom = 16.dp) ,
                contentScale = ContentScale.Crop// Adjust space below the logo
            )

            Spacer(modifier = Modifier.height(16.dp))
            TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.login(username, password,
                    onSuccess = { userid, role ->
                        if (role == "user") {
                            onLoginSuccess(userid, role)
                        } else {
                            toastMessage = "Admin can't login using mobile"
                            showToast = true // Trigger the toast
                        }
                    },
                    onError = { errorMessage = it }
                )
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Login")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }
    }
}

