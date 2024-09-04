package com.example.pokemon.ui.pokemon.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
import com.example.pokemon.ui.pokemon.state.PokemonScreenUIState
import com.example.pokemon.ui.pokemon.view.components.PokemonListingItem
import com.example.pokemon.ui.pokemon.view.components.SearchBar
import com.example.pokemon.ui.theme.PokemonTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun PokemonScreen(
    uiState: StateFlow<PokemonScreenUIState>,
    onEvent: (PokemonScreenEvent) -> Unit,
    pokemon: LazyPagingItems<PokemonDTO>
) {

    val listState = rememberLazyListState()
    var hideKeyboard by remember { mutableStateOf(false) }
    val state by uiState.collectAsState()

    LaunchedEffect(key1 = state.shouldScrollToTop) {
        listState.animateScrollToItem(0)
        onEvent(PokemonScreenEvent.SetScrollToIdToFalse)
    }

    LaunchedEffect(key1 = state.scrollToPokemonById.first, block = {
        if (state.scrollToPokemonById.first) {
            listState.animateScrollToItem(state.scrollToPokemonById.second-1)
            onEvent(PokemonScreenEvent.SetScrollToIdToFalse)
            hideKeyboard = true
        }
    })

    Scaffold(
        topBar = { TopAppBar(title = "Pokemon", onBack = null) },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { hideKeyboard = true }
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(1f),
                        onEvent = onEvent,
                        suggestions = state.searchedTvShows,
                        hideKeyboard = hideKeyboard,
                        onFocusClear = { hideKeyboard = false }
                    )

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.padding(top = 60.dp)
                    ) {
                        items(pokemon.itemCount) {
                            PokemonListingItem(
                                pokemon = pokemon[it]!!.toPokemon(),
                                onPokemonClicked = { name ->
                                    onEvent(PokemonScreenEvent.OnPokemonClicked(name))
                                }
                            )
                        }
                    }
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
    )
}

@ThemePreview
@Composable
fun PokemonScreenPreview() {
    Surface {
        PokemonTheme {
            PokemonScreen(
                uiState = MutableStateFlow(PokemonScreenUIState(searchedTvShows = emptyList())),
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