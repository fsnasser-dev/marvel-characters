package dev.fsnasser.marvelcharacters.utils

import dev.fsnasser.marvelcharacters.data.entities.CharacterApi
import dev.fsnasser.marvelcharacters.data.entities.FavoriteCharacter
import dev.fsnasser.marvelcharacters.ui.entities.Character

object LocalTestUtil {

    fun createCharacterApiDataResult(id: Int = 0) = CharacterApi.Data.Result(
        123 + id.toLong(),
        "Iron Man $id",
        "Description test $id",
        CharacterApi.Data.Result.Thumbnail(
            "http://www.mock.com/thumbnail$id",
            "jpg"
        )
    )

    fun createCharacterApi(count: Int) = CharacterApi(
        "",
        CharacterApi.Data(
            (0 until count).map {
                createCharacterApiDataResult(it)
            }
        )
    )

    fun createCharacterApiWithEmptyResult() = CharacterApi(
        "",
        CharacterApi.Data(ArrayList())
    )

    fun createFavoriteCharacter(id: Long = 123, name: String = "Iron Man",
                                isFavorite: Boolean = true) = FavoriteCharacter(
        id,
        "http://www.mock.com/thumbnail",
        "jpg",
        name,
        isFavorite,
        "Description test"
    )

    fun createFavoriteCharacterList(count: Int, id: Long = 123, name: String = "Iron Man",
                                    isFavorite: Boolean = true): List<FavoriteCharacter> {
        return (0 until count).map {
            createFavoriteCharacter(
                id + it,
                name + it,
                isFavorite
            )
        }
    }

    fun createCharacter(id: Long = 123, name: String = "Iron Man",
                        isFavorite: Boolean = true) = Character(
        id,
        "http://www.mock.com/thumbnail",
        "jpg",
        name,
        isFavorite,
        "Description test"
    )

}