package com.example.pokemon.di

import com.example.pokemon.data.api.remote.PokemonApi
import com.example.pokemon.domain.api.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun providePokemonRepository(pokemonApi: PokemonApi): PokemonRepository {
        return PokemonRepository(pokemonApi)
    }
}