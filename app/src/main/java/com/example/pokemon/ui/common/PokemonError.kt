package com.example.pokemon.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.ui.theme.spacing8

@Composable
fun PokemonError(error: String, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(spacing8),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Something went wrong\n$error", style = MaterialTheme.typography.headlineMedium)
    }
}

@ThemePreview
@Composable
fun PokemonErrorPreview() {
    PokemonTheme {
        PokemonError(error = "Failed to load pokemon", modifier = Modifier)
    }
}