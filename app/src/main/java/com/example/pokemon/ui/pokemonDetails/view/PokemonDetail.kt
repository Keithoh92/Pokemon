package com.example.pokemon.ui.pokemonDetails.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.pokemon.ui.pokemon.event.PokemonScreenEvent
import com.example.pokemon.ui.pokemon.state.PokemonScreenUIState
import com.example.pokemon.ui.pokemon.view.components.PokemonListingItem
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PokemonDetail(
    uiState: StateFlow<PokemonScreenUIState>,
    onEvent: (PokemonScreenEvent) -> Unit
) {

    val state by uiState.collectAsState()

//    Column(modifier = Modifier.fillMaxSize()) {
//        PokemonListingItem(pokemon = state.pokemon[state.selectedPokemon])
//
//
//    }
}