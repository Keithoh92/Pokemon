package com.example.pokemon.ui.pokemon.view.model

data class PokemonStat(
    val baseStat: Int,
    val effort: Int,
    val stat: Stat
)

data class Stat(
    val name: String,
    val url: String
)
