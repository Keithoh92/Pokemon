package com.example.pokemon.ui.pokemon.effect

sealed class PokemonScreenEffect {
    sealed class Navigation : PokemonScreenEffect() {
        data class OnNavigateToPokemonDetails(val name: String) : PokemonScreenEffect()
    }
}