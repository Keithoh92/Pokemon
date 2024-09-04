package com.example.pokemon.domain.api.repository

import android.content.ContentValues
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pokemon.common.Resource
import com.example.pokemon.data.api.dto.PokemonDTO
import com.example.pokemon.data.api.dto.PokemonDetailsAPIResponse
import com.example.pokemon.data.api.remote.PokemonApi
import com.example.pokemon.data.api.paging.PokemonApiPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class PokemonRepository(private val pokemonApi: PokemonApi) {

    fun getPokemonStream(): Flow<PagingData<PokemonDTO>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { PokemonApiPagingSource(pokemonApi) }
        ).flow
    }

    suspend fun getPokemonDetails(url: String): Flow<Resource<PokemonDetailsAPIResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = pokemonApi.downloadPokemonDetails(url)
            val details = response.body()

            if (response.isSuccessful && details != null) {
                emit(Resource.Success(details))
            } else {
                emit(Resource.Error("Failed to load details"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check internet connection: ${e.message}"))
            Log.e(ContentValues.TAG, "Couldn't reach server, check internet connection: ${e.message}")
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Http Exception, unexpected response: ${e.code()} ${e.message}"))
            Log.e(ContentValues.TAG, "Http Exception, unexpected response: ${e.code()} ${e.message}")
        }
    }
}