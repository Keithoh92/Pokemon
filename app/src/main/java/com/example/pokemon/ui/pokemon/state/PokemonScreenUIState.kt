package com.example.pokemon.ui.pokemon.state

data class PokemonScreenUIState(
    val shouldScrollToTop: Boolean = false,
    val scrollToPokemonById: Pair<Boolean, Int> = Pair(false, -1),
    val searchedTvShows: List<Pair<Int, String>> = emptyList()
)
