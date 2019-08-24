package dev.fsnasser.marvelcharacters.ui.entities

import java.io.Serializable

data class Character (
    val id: Long,
    val thumbnail: String?,
    val thumbnailExt: String?,
    val name: String,
    val isFavorite: Boolean,
    val description: String?,
    val comics: List<ComicSerie>? = null,
    val series: List<ComicSerie>? = null

) : Serializable {

    data class ComicSerie (
        val title: String,
        val thumbnail: String?
    )

}