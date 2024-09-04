package com.example.pokemon.ui.pokemon.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pokemon.data.api.dao.PokemonDTO
import com.example.pokemon.domain.toPokemon
import com.example.pokemon.ui.common.PokemonError
import com.example.pokemon.ui.common.ThemePreview
import com.example.pokemon.ui.common.TopAppBar
import com.example.pokemon.ui.pokemon.event.PokemonScreenEvent
import com.example.pokemon.ui.pokemon.view.components.PokemonListingItem
import com.example.pokemon.ui.theme.PokemonTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun PokemonScreen(
    onEvent: (PokemonScreenEvent) -> Unit,
    pokemon: LazyPagingItems<PokemonDTO>
) {
    Scaffold(
        topBar = { TopAppBar(title = "Pokemon", onBack = null) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                ) {
                    println("Size of list = ${pokemon.itemCount}")
                    items(pokemon.itemCount) {
                        PokemonListingItem(
                            pokemon = pokemon[it]!!.toPokemon(),
                            onPokemonClicked = { name ->
                                onEvent(PokemonScreenEvent.OnPokemonClicked(name))
                            }
                        )
                    }
                }
                pokemon.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LinearProgressIndicator()
                                Text(text = "Loading, please wait")
                            }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val error = pokemon.loadState.refresh as LoadState.Error
                            PokemonError(error = error.error.localizedMessage ?: "Error", modifier = Modifier)
                        }
                        loadState.append is LoadState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LinearProgressIndicator()
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            val error = pokemon.loadState.refresh as LoadState.Error
                            PokemonError(error = error.error.localizedMessage ?: "Error", modifier = Modifier)
                        }
                    }
                }
            }
        }
    )
}

@ThemePreview
@Composable
fun PokemonScreenPreview() {
    Surface {
        PokemonTheme {
            PokemonScreen(
                onEvent = {},
                pokemon = flowOf(PagingData.from(
                    listOf(
                        PokemonDTO("Charizad", ""),
                        PokemonDTO("Charizad", ""),
                        PokemonDTO("Charizad", ""),
                        PokemonDTO("Charizad", ""),
                        PokemonDTO("Charizad", ""),
                        PokemonDTO("Charizad", ""),
                        PokemonDTO("Charizad", ""),
                    )
                )
                ).collectAsLazyPagingItems()
            )
        }
    }
}