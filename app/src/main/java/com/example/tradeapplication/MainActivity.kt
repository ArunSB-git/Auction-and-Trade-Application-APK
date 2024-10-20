package com.example.tradeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.tradeapplication.ui.theme.TradeApplicationTheme
import coil.compose.rememberImagePainter

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController= rememberNavController()

            TradeApplicationTheme {
                // A surface container using the 'background' color from the theme
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
fun RecipeScreen(modifier: Modifier = Modifier, navigationToDetails: (Player) -> Unit, viewstate: MainViewModel.ReceipeState) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewstate.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewstate.error != null -> {
                Text("ERROR: ${viewstate.error}", modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                PlayerScreen(categories = viewstate.list, navigationToDetails)
            }
        }
    }
}


@Composable
fun PlayerScreen(categories: List<Player>,navigationToDetails:(Player)->Unit){
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxSize()){
        items(categories){
                category ->
            PlayerItem(category = category,navigationToDetails)
        }
    }
}
// How each Items looks like
@Composable
fun PlayerItem(category: Player, navigationToDetails: (Player) -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable { navigationToDetails(category) }
            .border(3.dp, Color.Blue) // Adjust border width and color here
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(category.playerImage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
            )

            Text(
                text = category.playername.uppercase(),
                color = Color.Yellow,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
fun PlayerApp(navController: NavHostController) {

    val recipeViewModel:MainViewModel = viewModel()
    val viewState by recipeViewModel._playersState

    NavHost(navController = navController, startDestination = Screen.playerScreen.route) {
        composable(route = Screen.playerScreen.route) {
            RecipeScreen(viewstate = viewState, navigationToDetails = { player ->
                navController.currentBackStackEntry?.savedStateHandle?.set("selectedPlayer", player)
                navController.navigate(Screen.detailedScreen.route)
            })
        }
        composable(route = Screen.detailedScreen.route) {
            val player = navController.previousBackStackEntry?.savedStateHandle?.get<Player>("selectedPlayer") ?: Player(0, "", "", "", "", 0)
            CategoryDetailScreen(category = player, navController = navController)
        }
    }

}