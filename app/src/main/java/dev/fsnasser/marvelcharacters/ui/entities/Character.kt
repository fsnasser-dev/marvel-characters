package dev.fsnasser.marvelcharacters.ui.entities

data class Character (
    val id: Long,
    val image: String?,
    val name: String,
    val isFavorite: Boolean
)