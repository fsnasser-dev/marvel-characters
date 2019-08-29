package dev.fsnasser.marvelcharacters.utils.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.fsnasser.marvelcharacters.data.db.CharactersDao
import dev.fsnasser.marvelcharacters.data.entities.FavoriteCharacter

@Database(entities = [FavoriteCharacter::class], version = 1, exportSchema = false)
abstract class MarvelCharactersDatabase : RoomDatabase() {

    abstract fun charactersDao(): CharactersDao

    companion object {
        const val DATABASE_NAME = "marvelcharacters.db"
    }

}