package com.example.pokemon.data.api.dao

data class StatsDTO(
    val baseStat: Int,
    val effort: Int,
    val stat: StatDTO
)

data class StatDTO(
    val name: String,
    val url: String
)
