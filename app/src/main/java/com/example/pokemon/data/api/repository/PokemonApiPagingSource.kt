package com.example.pokemon.data.api.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokemon.data.api.dao.PokemonDTO
import com.example.pokemon.data.api.remote.PokemonApi
import java.io.IOException

class PokemonApiPagingSource(
    private val pokemonApi: PokemonApi
) : PagingSource<Int, PokemonDTO>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonDTO> {
        val position = params.key ?: 0
        return try {
            val response = pokemonApi.downloadPokemon(params.loadSize, position)
            val apiResponseResults = response.results

            LoadResult.Page(
                data = apiResponseResults,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = if (response.next == null) null else position + params.loadSize
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize)
                ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
    }
}