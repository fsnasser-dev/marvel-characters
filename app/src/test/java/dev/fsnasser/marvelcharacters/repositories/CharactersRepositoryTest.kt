package dev.fsnasser.marvelcharacters.repositories

import dev.fsnasser.marvelcharacters.data.db.CharactersLocalDataSource
import dev.fsnasser.marvelcharacters.data.entities.CharacterApi
import dev.fsnasser.marvelcharacters.data.repositories.CharactersRepository
import dev.fsnasser.marvelcharacters.data.rest.characters.CharactersRemoteDataSource
import dev.fsnasser.marvelcharacters.utils.LocalTestUtil.createCharacter
import dev.fsnasser.marvelcharacters.utils.LocalTestUtil.createCharacterApi
import dev.fsnasser.marvelcharacters.utils.LocalTestUtil.createCharacterApiWithEmptyResult
import dev.fsnasser.marvelcharacters.utils.LocalTestUtil.createFavoriteCharacter
import dev.fsnasser.marvelcharacters.utils.LocalTestUtil.createFavoriteCharacterList
import dev.fsnasser.marvelcharacters.utils.data.Resource
import dev.fsnasser.marvelcharacters.utils.extensions.plusAssign
import dev.fsnasser.marvelcharacters.utils.helpers.NetworkHelper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class CharactersRepositoryTest {

    @Mock internal lateinit var mMockedNetworkHelper: NetworkHelper

    @Mock internal lateinit var mMockedRemoteDataSource: CharactersRemoteDataSource

    @Mock internal lateinit var mMockedLocalDataSource: CharactersLocalDataSource

    @InjectMocks internal lateinit var mCharactersRepository: CharactersRepository

    private lateinit var mCompositeDisposable: CompositeDisposable

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mCompositeDisposable = CompositeDisposable()
    }

    @Test
    fun getAll_WhenSearchNull_ReturnSuccess() {
        val characterApiMock = createCharacterApi(10)
        `when`(mMockedRemoteDataSource.getAll(0, 20, null, "name"))
            .thenReturn(Observable.just(Response.success(characterApiMock)))
        `when`(mMockedNetworkHelper.isConnectedToInternet).thenReturn(true)

        mCompositeDisposable += mCharactersRepository.getAll(0, 20, null, "name")
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {
                override fun onNext(resource: Resource<CharacterApi>) {
                    assertEquals(Resource.Status.SUCCESS, resource.status)
                    resource.response?.data?.results?.let {
                        assertEquals(characterApiMock.data.results[0].id, it[0].id)
                    } ?: run {
                        fail("Lista de personagens vazia.")
                    }
                }

                override fun onError(e: Throwable) {
                    fail("Retornou um erro: ${e.message}.")
                }

                override fun onComplete() {}
            })
    }

    @Test
    fun getAll_WhenSearchNotNull_SearchFound_ReturnSuccess() {
        val characterApiMock = createCharacterApi(10)
        `when`(mMockedRemoteDataSource.getAll(0, 20, "test", "name"))
            .thenReturn(Observable.just(Response.success(characterApiMock)))
        `when`(mMockedNetworkHelper.isConnectedToInternet).thenReturn(true)

        mCompositeDisposable += mCharactersRepository.getAll(0, 20, "test", "name")
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {
                override fun onNext(resource: Resource<CharacterApi>) {
                    assertEquals(Resource.Status.SUCCESS, resource.status)
                    resource.response?.data?.results?.let {
                        assertEquals(characterApiMock.data.results[0].id, it[0].id)
                    } ?: run {
                        fail("Lista de personagens vazia.")
                    }
                }

                override fun onError(e: Throwable) {
                    fail("Retornou um erro: ${e.message}.")
                }

                override fun onComplete() {}
            })
    }

    @Test
    fun getAll_WhenSearchNotNull_SearchNotFound_ReturnNoData() {
        val characterApiMock = createCharacterApiWithEmptyResult()
        `when`(mMockedRemoteDataSource.getAll(0, 20, "test1", "name"))
            .thenReturn(Observable.just(Response.success(characterApiMock)))
        `when`(mMockedNetworkHelper.isConnectedToInternet).thenReturn(true)

        mCompositeDisposable += mCharactersRepository.getAll(0, 20, "test1", "name")
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {
                override fun onNext(resource: Resource<CharacterApi>) {
                    assertEquals(Resource.Status.NO_DATA, resource.status)
                    assertNull(resource.response?.data?.results)
                }

                override fun onError(e: Throwable) {
                    fail("Retornou um erro: ${e.message}.")
                }

                override fun onComplete() {}
            })
    }

    @Test
    fun getAll_WhenApiReturns500_ReturnError() {
        `when`(mMockedRemoteDataSource.getAll(0, 20, null, "name"))
            .thenReturn(Observable.just(Response.error(500, mock(ResponseBody::class.java))))
        `when`(mMockedNetworkHelper.isConnectedToInternet).thenReturn(true)

        mCompositeDisposable += mCharactersRepository.getAll(0, 20, null, "name")
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {
                override fun onNext(resource: Resource<CharacterApi>) {
                    assertEquals(Resource.Status.ERROR, resource.status)
                    assertNull(resource.response)
                }

                override fun onError(e: Throwable) {
                    fail("Retornou um erro: ${e.message}.")
                }

                override fun onComplete() {}
            })
    }

    @Test
    fun getAll_WhenApiReturns409_ReturnError() {
        `when`(mMockedRemoteDataSource.getAll(0, 101, null, "name"))
            .thenReturn(Observable.just(Response.error(409, mock(ResponseBody::class.java))))
        `when`(mMockedNetworkHelper.isConnectedToInternet).thenReturn(true)

        mCompositeDisposable += mCharactersRepository.getAll(0, 101, null, "name")
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {
                override fun onNext(resource: Resource<CharacterApi>) {
                    assertEquals(Resource.Status.ERROR, resource.status)
                    assertNull(resource.response)
                }

                override fun onError(e: Throwable) {
                    fail("Retornou um erro: ${e.message}.")
                }

                override fun onComplete() {}
            })
    }

    @Test
    fun getAll_WhenApiReturns401_ReturnError() {
        `when`(mMockedRemoteDataSource.getAll(0, 20, null, "name"))
            .thenReturn(Observable.just(Response.error(401, mock(ResponseBody::class.java))))
        `when`(mMockedNetworkHelper.isConnectedToInternet).thenReturn(true)

        mCompositeDisposable += mCharactersRepository.getAll(0, 20, null, "name")
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {
                override fun onNext(resource: Resource<CharacterApi>) {
                    assertEquals(Resource.Status.ERROR, resource.status)
                    assertNull(resource.response)
                }

                override fun onError(e: Throwable) {
                    fail("Retornou um erro: ${e.message}.")
                }

                override fun onComplete() {}
            })
    }

    @Test
    fun getAll_WhenNoInternet_ReturnNotConnected() {
        `when`(mMockedNetworkHelper.isConnectedToInternet).thenReturn(false)

        mCompositeDisposable += mCharactersRepository.getAll(0, 20, null, "name")
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {
                override fun onNext(resource: Resource<CharacterApi>) {
                    assertEquals(Resource.Status.NOT_CONNECTED, resource.status)
                    assertNull(resource.response)
                }

                override fun onError(e: Throwable) {
                    fail("Retornou um erro: ${e.message}.")
                }

                override fun onComplete() {}
            })
    }

    @Test
    fun getAllFavouriteCharacters() {
        val mockFavouriteCharacters = createFavoriteCharacterList(3)
        `when`(mMockedLocalDataSource.getAllFavouriteCharacters())
            .thenReturn(Single.just(mockFavouriteCharacters))

        mCompositeDisposable += mCharactersRepository.getAllFavouriteCharacters()
            .subscribe({
                assertEquals(mockFavouriteCharacters.size, it.size)
                assertEquals(mockFavouriteCharacters[0].id, it[0].id)
            },
            {
                fail("Retornou um erro: ${it.message}.")
            })
    }

    @Test
    fun isFavouriteCharacter_WhenIsFavorite_ReturnTrue() {
        `when`(mMockedLocalDataSource.isFavouriteCharacter(123)).thenReturn(true)

        assertTrue(mCharactersRepository.isFavouriteCharacter(123))
    }

    @Test
    fun insertCharacterIntoFavourites_WhenSuccess_ReturnTrue() {
        `when`(mMockedLocalDataSource.insertCharacterIntoFavourites(createFavoriteCharacter()))
            .thenReturn(371293)

        assertTrue(mCharactersRepository.insertCharacterIntoFavourites(createCharacter()))
    }

    @Test
    fun insertCharacterIntoFavourites_WhenError_ReturnFalse() {
        `when`(mMockedLocalDataSource.insertCharacterIntoFavourites(createFavoriteCharacter()))
            .thenReturn(0)

        assertFalse(mCharactersRepository.insertCharacterIntoFavourites(createCharacter()))
    }

    @Test
    fun removeCharacterFromFavourites_WhenSuccess_ReturnTrue() {
        `when`(mMockedLocalDataSource.removeCharacterFromFavourites(createFavoriteCharacter()))
            .thenReturn(1)

        assertTrue(mCharactersRepository.removeCharacterFromFavourites(createCharacter()))
    }

    @Test
    fun removeCharacterFromFavourites_WhenError_ReturnFalse() {
        `when`(mMockedLocalDataSource.insertCharacterIntoFavourites(createFavoriteCharacter()))
            .thenReturn(0)

        assertFalse(mCharactersRepository.removeCharacterFromFavourites(createCharacter()))
    }

    @After
    fun after() {
        if(!mCompositeDisposable.isDisposed) mCompositeDisposable.dispose()
    }

}