package com.example.pokemon.ui.pokemon.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pokemon.ui.common.ThemePreview
import com.example.pokemon.ui.pokemon.view.model.Pokemon
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.ui.theme.full
import com.example.pokemon.ui.theme.spacing12
import com.example.pokemon.ui.theme.spacing8

@Composable
fun PokemonListingItem(
    pokemon: Pokemon,
    onPokemonClicked: (name: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Gray))
            .padding(spacing8)
            .clickable {
                onPokemonClicked(pokemon.name)
            },
        horizontalArrangement = Arrangement.spacedBy(spacing12)
    ) {
        Text(
            text = pokemon.name,
            modifier = Modifier.padding(vertical = spacing12),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(full))
        IconButton(onClick = { onPokemonClicked(pokemon.name) }) {
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "arrow down")
        }
    }

}

@ThemePreview
@Composable
fun PokemonScreenPreview() {
    PokemonTheme {
        PokemonListingItem(
            pokemon = Pokemon(1, "Charizad", ""),
            onPokemonClicked = {}
        )
    }
}