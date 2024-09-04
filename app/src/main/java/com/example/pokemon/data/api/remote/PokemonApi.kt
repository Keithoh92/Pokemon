package com.example.pokemon.data.api.remote

import com.example.pokemon.data.api.dto.PokemonAPIResponse
import com.example.pokemon.data.api.dto.PokemonDetailsAPIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun downloadPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonAPIResponse

    @GET("pokemon/{name}")
    suspend fun downloadPokemonDetails(@Path("name") name: String): Response<PokemonDetailsAPIResponse>

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}