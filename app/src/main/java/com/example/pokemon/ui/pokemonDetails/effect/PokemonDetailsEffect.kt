package com.example.pokemon.ui.pokemonDetails.effect

sealed class PokemonDetailsEffect {
    sealed class Navigation : PokemonDetailsEffect() {
        data object OnBack : Navigation()
    }
}