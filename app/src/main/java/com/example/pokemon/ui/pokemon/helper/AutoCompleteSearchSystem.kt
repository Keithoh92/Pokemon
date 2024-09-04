package com.example.pokemon.ui.pokemon.helper

import com.example.pokemon.ui.pokemon.view.model.TrieNode

class AutoCompleteSearchSystem {
    private val root = TrieNode()

    fun insert(word: String) {
        var current = root
        for (char in word) {
            val node = current.children.getOrPut(char) { TrieNode() }
            current = node
        }
        current.isEndOfWord = true
    }

    fun search(prefix: String): List<String> {
        var result = mutableListOf<String>()
        var current = root
        for (char in prefix) {
            val node = current.children[char] ?: return result
            current = node
        }
        dfs(current, prefix, result)
        return result
    }

    private fun dfs(node: TrieNode, prefix: String, result: MutableList<String>) {
        if (node.isEndOfWord) result.add(prefix)

        for ((char, child) in node.children) {
            dfs(child, prefix + char, result)
        }
    }
}