package com.example.pokemon.ui.pokemonDetails.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokemon.ui.pokemonDetails.PokemonDetailsScreenMain
import com.example.pokemon.ui.pokemonDetails.viewmodel.PokemonDetailsViewModel

const val pokemonDetailsRoute = "pokemon_details_route"
const val pokemonDetailsArgs = "pokemon_details_args"

fun NavController.navigateToPokemonDetailsScreen(
    pokemonName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$pokemonDetailsRoute/${pokemonName}", navOptions)
}

fun NavGraphBuilder.pokemonDetailsScreen(onBack: () -> Unit) {
    composable(
        route = "$pokemonDetailsRoute/{$pokemonDetailsArgs}",
        arguments = listOf(navArgument(pokemonDetailsArgs) { type = NavType.StringType })
    ) { navBackStackEntry ->
        val viewModel = hiltViewModel<PokemonDetailsViewModel>()
        val pokemonName = navBackStackEntry.arguments?.getString(pokemonDetailsArgs) ?: ""

        PokemonDetailsScreenMain(viewModel = viewModel, pokemonName, onBack)
    }
}