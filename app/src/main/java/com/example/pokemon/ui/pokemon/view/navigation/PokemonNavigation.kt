package com.example.pokemon.ui.pokemon.view.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.pokemon.ui.pokemon.view.PokemonMain
import com.example.pokemon.ui.pokemon.viewmodel.PokemonScreenViewModel

const val pokemonRoute = "pokemon_route"

fun NavGraphBuilder.pokemonScreen(onPokemonClicked: (name: String) -> Unit) {
    composable(route = pokemonRoute) {
        val viewModel = hiltViewModel<PokemonScreenViewModel>()
        PokemonMain(viewModel = viewModel, onPokemonClicked = { name -> onPokemonClicked(name) })
    }
}