package com.example.pokemon.ui.pokemon.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pokemon.ui.pokemon.effect.PokemonScreenEffect
import com.example.pokemon.ui.pokemon.viewmodel.PokemonScreenViewModel

@Composable
fun PokemonMain(
    viewModel: PokemonScreenViewModel,
    onPokemonClicked: (name: String) -> Unit
) {
    val pokemon = viewModel.pokemonState.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PokemonScreenEffect.Navigation.OnNavigateToPokemonDetails ->
                    onPokemonClicked(effect.name)
            }
        }
    }

    PokemonScreen(uiState = viewModel.uiState, viewModel::onEvent, pokemon)
}