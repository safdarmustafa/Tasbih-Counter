package com.example.tasbihcounter

data class Dua(
    val title: String,
    val arabic: String,
    val transliteration: String,
    val meaning: String,
    val reference: String
)

data class DuaCategory(
    val name: String,
    val duas: List<Dua>
)
