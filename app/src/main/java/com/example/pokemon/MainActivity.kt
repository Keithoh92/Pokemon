package com.example.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.pokemon.ui.pokemon.view.navigation.pokemonRoute
import com.example.pokemon.ui.pokemon.view.navigation.pokemonScreen
import com.example.pokemon.ui.pokemonDetails.navigation.navigateToPokemonDetailsScreen
import com.example.pokemon.ui.pokemonDetails.navigation.pokemonDetailsScreen
import com.example.pokemon.ui.theme.PokemonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                Surface(modifier = Modifier, color = Color.White) {
                    navController = rememberNavController()
                    NavHost(navController = navController, pokemonRoute) {
                        pokemonScreen(
                            onPokemonClicked = { pokemonName ->
                                navController.navigateToPokemonDetailsScreen(pokemonName)
                            }
                        )

                        pokemonDetailsScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokemonTheme {

    }
}