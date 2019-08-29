package dev.fsnasser.marvelcharacters.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.fsnasser.marvelcharacters.data.db.CharactersDao
import dev.fsnasser.marvelcharacters.utils.TestUtil
import dev.fsnasser.marvelcharacters.utils.data.MarvelCharactersDatabase
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharactersDaoTest {

    private lateinit var db: MarvelCharactersDatabase
    private lateinit var charactersDao: CharactersDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MarvelCharactersDatabase::class.java).build()
        charactersDao = db.charactersDao()
    }

    @Test
    fun favoriteCharacterSelection() {
        val favoriteCharacters = TestUtil.createFavoriteCharacterList(3)
        favoriteCharacters.forEach {
            charactersDao.insert(it)
        }

        charactersDao.getAllFavouriteCharacters().doOnSuccess {
            assertEquals(it.size, 3)
        }
    }

    @Test
    fun favoriteCharacterInsertion() {
        val favoriteCharacter = TestUtil.createFavoriteCharacter()
        charactersDao.insert(favoriteCharacter)

        val count = charactersDao.isFavouriteCharacter(favoriteCharacter.id)
        assertEquals(count, 1)
    }

    @Test
    fun favoriteCharacterInsertion_WhenConflict_Replace() {
        val favoriteCharacter = TestUtil.createFavoriteCharacter()
        charactersDao.insert(favoriteCharacter)
        charactersDao.insert(favoriteCharacter)

        val count = charactersDao.isFavouriteCharacter(favoriteCharacter.id)
        assertEquals(count, 1)
    }

    @Test
    fun favoriteCharactersRemoval() {
        val favoriteCharacter = TestUtil.createFavoriteCharacter()
        charactersDao.insert(favoriteCharacter)

        charactersDao.remove(favoriteCharacter)

        val count = charactersDao.isFavouriteCharacter(favoriteCharacter.id)
        assertEquals(count, 0)
    }

    @After
    fun closeDb() {
        db.close()
    }


}