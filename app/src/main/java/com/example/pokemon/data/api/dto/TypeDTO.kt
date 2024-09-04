package com.example.pokemon.data.api.dto

data class TypeDTO(
    val slot: Int,
    val type: TypeDetailDTO
)

data class TypeDetailDTO(
    val name: String
)
