package com.example.pokemon.ui.pokemon.view.model

data class PokemonDetails(
    val id: Int,
    val name: String,
    val weight: Int,
    val height : Int,
    val type: List<PokemonType>,
    val stats: List<PokemonStat>,
    val sprite: List<String>
)
