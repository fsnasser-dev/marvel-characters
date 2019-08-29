package dev.fsnasser.marvelcharacters.data.db

import dev.fsnasser.marvelcharacters.data.entities.FavoriteCharacter
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharactersLocalDataSource @Inject constructor(private val charactersDao: CharactersDao) {

    fun getAllFavouriteCharacters(): Single<List<FavoriteCharacter>> =
        charactersDao.getAllFavouriteCharacters()

    fun isFavouriteCharacter(id: Long): Boolean = charactersDao.isFavouriteCharacter(id) > 0

    fun insertCharacterIntoFavourites(favoriteCharacter: FavoriteCharacter): Long {
        favoriteCharacter.isFavorite = true
        return charactersDao.insert(favoriteCharacter)
    }

    fun removeCharacterFromFavourites(favoriteCharacter: FavoriteCharacter): Int {
        favoriteCharacter.isFavorite = false
        return charactersDao.remove(favoriteCharacter)
    }

}