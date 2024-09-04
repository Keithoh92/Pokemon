package com.example.pokemon.ui.pokemonDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.pokemon.ui.pokemonDetails.effect.PokemonDetailsEffect
import com.example.pokemon.ui.pokemonDetails.event.PokemonDetailsEvent
import com.example.pokemon.ui.pokemonDetails.viewmodel.PokemonDetailsViewModel

@Composable
fun PokemonDetailsScreenMain(
    viewModel: PokemonDetailsViewModel,
    pokemonName: String,
    onBack: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(PokemonDetailsEvent.FetchPokemonDetails(pokemonName))
        viewModel.effect.collect { onBack() }
    }
    PokemonDetailsScreen(viewModel.uiState, onBack)
}