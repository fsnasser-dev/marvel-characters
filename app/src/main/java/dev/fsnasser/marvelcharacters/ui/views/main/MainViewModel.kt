package dev.fsnasser.marvelcharacters.ui.views.main

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.data.entities.CharacterApi
import dev.fsnasser.marvelcharacters.data.entities.FavoriteCharacter
import dev.fsnasser.marvelcharacters.data.repositories.CharactersRepository
import dev.fsnasser.marvelcharacters.ui.entities.Character
import dev.fsnasser.marvelcharacters.utils.data.Resource
import dev.fsnasser.marvelcharacters.utils.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mContext: Context,
    private val mRepository: CharactersRepository) : ViewModel() {

    val charactersStatus = ObservableField<Resource.Status>()

    val favoritesStatus = ObservableField<Resource.Status>()

    val charactersWarningMessage = ObservableField<String>()

    val favoritesWarningMessage = ObservableField<String>()

    val characters = MutableLiveData<List<Character>>()

    val search = MutableLiveData<String>()

    val favoriteCharacters = MutableLiveData<List<FavoriteCharacter>>()

    val updateFavoriteItemId = MutableLiveData<Long>()

    private val mCompositeDisposable = CompositeDisposable()

    fun getAll(offset: Int, limit: Int, nameStartsWith: String? = null,
               orderBy: String = "name", refreshing: Boolean = false) {
        if(refreshing) charactersStatus.set(Resource.Status.REFRESHING)
        else charactersStatus.set(Resource.Status.LOADING)

        mCompositeDisposable += mRepository
            .getAll(offset, limit, nameStartsWith, orderBy)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {

                override fun onNext(data: Resource<CharacterApi>) {
                    val dataStatus = data.status
                    charactersStatus.set(dataStatus)

                    when(dataStatus) {
                        Resource.Status.SUCCESS -> {
                            characters.value = convertToCharacter(data.response?.data?.results)
                            return
                        }
                        Resource.Status.NO_DATA -> {
                            nameStartsWith?.let {
                                charactersWarningMessage.set(mContext.getString(R.string.character_term_not_found, it))
                            }
                        }
                        Resource.Status.NOT_CONNECTED -> {
                            charactersWarningMessage.set(mContext.getString(R.string.offline_msg))
                        }
                        else -> {
                            charactersWarningMessage.set(mContext.getString(R.string.character_list_error_msg))
                        }
                    }

                    characters.value = ArrayList()
                }

                override fun onError(e: Throwable) {
                    charactersStatus.set(Resource.Status.ERROR)
                    characters.value = ArrayList()
                }

                override fun onComplete() {}
            })
    }

    fun addToFavorites(character: Character) = mRepository.insertCharacterIntoFavourites(character)

    fun removeFromFavorites(character: Character) = mRepository.removeCharacterFromFavourites(character)

    fun getAllFavorites() {
        mCompositeDisposable += mRepository
            .getAllFavouriteCharacters()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ favoriteCharactersResult ->
                if(favoriteCharactersResult.isNotEmpty()) {
                    favoritesStatus.set(Resource.Status.SUCCESS)
                    favoriteCharacters.value = favoriteCharactersResult
                } else {
                    favoritesStatus.set(Resource.Status.NO_DATA)
                    favoritesWarningMessage.set(mContext.getString(R.string.favourites_empty_msg))
                    favoriteCharacters.value = ArrayList()
                }
            },
            {
                favoritesStatus.set(Resource.Status.ERROR)
                favoritesWarningMessage.set(mContext.getString(R.string.favourites_list_error_msg))
                favoriteCharacters.value = ArrayList()
            })
    }

    private fun convertToCharacter(results: List<CharacterApi.Data.Result>?) : List<Character> {
        val characters = ArrayList<Character>()

        results?.let {
            for(result in it) {
                characters.add(Character(result.id, result.thumbnail.path, result.thumbnail.extension,
                    result.name, mRepository.isFavouriteCharacter(result.id), result.description))
            }
        }

        return characters
    }

    override fun onCleared() {
        super.onCleared()

        if(!mCompositeDisposable.isDisposed) mCompositeDisposable.dispose()
    }

}