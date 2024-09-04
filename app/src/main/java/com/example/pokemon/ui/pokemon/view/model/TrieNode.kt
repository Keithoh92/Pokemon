package com.example.pokemon.ui.pokemon.view.model

data class TrieNode(
    val value: String = "",
    val children: MutableMap<Char, TrieNode> = mutableMapOf()
) {
    var isEndOfWord = false
}