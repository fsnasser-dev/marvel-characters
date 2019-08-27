package dev.fsnasser.marvelcharacters.data.entities

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_characters")
data class FavoriteCharacter (
    @PrimaryKey val id: Long,
    @Nullable val thumbnail: String?,
    @Nullable val thumbnailExt: String?,
    val name: String,
    var isFavorite: Boolean,
    @Nullable val description: String?

)