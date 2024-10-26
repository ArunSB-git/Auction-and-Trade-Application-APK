package com.example.tradeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tradeapplication.ui.theme.TradeApplicationTheme

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
    val recipeViewModel: MainViewModel = viewModel()
    val viewState by recipeViewModel._playersState

    NavHost(navController = navController, startDestination = Screen.playerScreen.route) {
        composable(route = Screen.playerScreen.route) {
            RecipeScreen(
                viewstate = viewState,
                navigationToDetails = { player ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("selectedPlayer", player)
                    navController.navigate(Screen.detailedScreen.route)
                }
            )
        }
        composable(route = Screen.detailedScreen.route) {
            val player = navController.previousBackStackEntry?.savedStateHandle?.get<Player>("selectedPlayer") ?: Player(0, "", "", "", "", 0)
            CategoryDetailScreen(category = player, navController = navController)
        }
    }
}