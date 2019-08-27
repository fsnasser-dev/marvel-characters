package dev.fsnasser.marvelcharacters.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.fsnasser.marvelcharacters.data.entities.FavoriteCharacter

@Database(entities = [FavoriteCharacter::class], version = 1, exportSchema = false)
abstract class MarvelCharactersDatabase : RoomDatabase() {

    abstract fun charactersDao(): CharactersDao

    companion object {
        const val DATABASE_NAME = "marvelcharacters.db"
    }

}