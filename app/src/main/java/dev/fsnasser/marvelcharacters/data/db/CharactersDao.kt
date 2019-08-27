package dev.fsnasser.marvelcharacters.data.db

import androidx.room.*
import io.reactivex.Single
import dev.fsnasser.marvelcharacters.data.entities.FavoriteCharacter

@Dao
interface CharactersDao {

    @Query("SELECT * FROM favourite_characters ORDER BY name")
    fun getAllFavouriteCharacters(): Single<List<FavoriteCharacter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: FavoriteCharacter)

    @Delete
    fun remove(character: FavoriteCharacter)

    @Query("SELECT count(*) FROM favourite_characters where id LIKE :id")
    fun isFavouriteCharacter(id: Long): Int

}