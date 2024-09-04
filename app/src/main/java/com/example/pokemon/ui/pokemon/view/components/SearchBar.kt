package com.example.pokemon.ui.pokemon.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemon.ui.pokemon.event.PokemonScreenEvent
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.ui.theme.spacing16

@Composable
fun SearchBar(
    modifier:  Modifier,
    onEvent: (PokemonScreenEvent) -> Unit,
    suggestions: List<Pair<Int, String>>,
    hideKeyboard: Boolean,
    onFocusClear: () -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier
    ) {
        Column {
            TextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    onEvent(PokemonScreenEvent.OnSearchTextChanged(newText))
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search...") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.LightGray.copy(alpha = 0.3f)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })

            )
            AnimatedVisibility(
                visible = suggestions.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.5f))
            ) {
                LazyColumn(
                    content = {
                    items(suggestions.size) {
                        val pokemon = suggestions[it]
                        Row(
                            modifier
                                .clickable {
                                    onEvent(PokemonScreenEvent.OnSelectSearchedTvShow(pokemon.first))
                                }
                                .padding(spacing16)
                        ) {
                            Text(
                                text = pokemon.second.replaceFirstChar { char -> char.titlecase() },
                                color = Color.Blue.copy(alpha = 0.5f)
                            )
                        }
                    }
                })
            }
        }
    }
    if (hideKeyboard || suggestions.isEmpty()) {
        focusManager.clearFocus()
        searchText = ""
        onFocusClear()
    }
}

@Composable
@Preview(showBackground = true)
fun SearchBarPreview() {
    PokemonTheme {
        SearchBar(
            modifier = Modifier,
            onEvent = {},
            suggestions = listOf(Pair(1, "Charizard"), Pair(2, "Weedle")),
            hideKeyboard = false,
            onFocusClear = {}
        )
    }
}