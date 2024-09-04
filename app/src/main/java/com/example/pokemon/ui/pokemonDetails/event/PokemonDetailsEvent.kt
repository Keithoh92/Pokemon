package com.example.pokemon.ui.pokemonDetails.event

sealed class PokemonDetailsEvent {
    data class FetchPokemonDetails(val name: String) : PokemonDetailsEvent()
    data object OnBack : PokemonDetailsEvent()
}