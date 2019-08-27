package dev.fsnasser.marvelcharacters.data.repositories

import dev.fsnasser.marvelcharacters.data.db.CharactersLocalDataSource
import dev.fsnasser.marvelcharacters.data.entities.CharacterApi
import dev.fsnasser.marvelcharacters.data.entities.FavoriteCharacter
import dev.fsnasser.marvelcharacters.data.rest.characters.CharactersRemoteDataSource
import dev.fsnasser.marvelcharacters.ui.entities.Character
import dev.fsnasser.marvelcharacters.utils.data.Resource
import dev.fsnasser.marvelcharacters.utils.helpers.NetworkHelper
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class CharactersRepository @Inject constructor(
    private val mNetworkHelper: NetworkHelper,
    private val mRemoteDataSource: CharactersRemoteDataSource,
    private val mLocalDataSource: CharactersLocalDataSource
) {

    fun getAll(offset: Int, limit: Int, nameStartsWith: String?, orderBy: String)
            : Observable<Resource<CharacterApi>> {
        if(mNetworkHelper.isConnectedToInternet) {
            return mRemoteDataSource.getAll(offset, limit, nameStartsWith, orderBy).flatMap {
                val body = it.body()

                val response = when(it.code()) {
                    200 -> {
                        if(body?.data?.results != null && body.data.results.isNotEmpty()) {
                            Resource.success(body)
                        } else if(nameStartsWith != null) {
                            Resource.noData()
                        } else Resource.error()
                    }
                    else -> Resource.error()
                }

                return@flatMap Observable.just(response)
            }
        } else return Observable.just(Resource.notConnected())
    }

    fun getAllFavouriteCharacters(): Single<List<FavoriteCharacter>> =
        mLocalDataSource.getAllFavouriteCharacters()

    fun isFavouriteCharacter(id: Long): Boolean = mLocalDataSource.isFavouriteCharacter(id)

    fun insertCharacterIntoFavourites(character: Character) {
        val characterDb = FavoriteCharacter(
            character.id, character.thumbnail, character.thumbnailExt,
            character.name, character.isFavorite, character.description)
        mLocalDataSource.insertCharacterIntoFavourites(characterDb)
    }

    fun removeCharacterFromFavourites(character: Character) {
        val characterDb = FavoriteCharacter(
            character.id, character.thumbnail, character.thumbnailExt,
            character.name, character.isFavorite, character.description)
        mLocalDataSource.removeCharacterFromFavourites(characterDb)
    }


}