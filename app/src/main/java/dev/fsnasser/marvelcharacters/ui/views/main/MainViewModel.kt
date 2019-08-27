package dev.fsnasser.marvelcharacters.ui.views.main

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val mRepository: CharactersRepository) : ViewModel() {

    companion object {
        private val TAG =  MainViewModel::class.java.simpleName
    }

    val status = ObservableField<Resource.Status>()

    val characters = MutableLiveData<List<Character>>()

    val search = MutableLiveData<String>()

    val favoriteCharacters = MutableLiveData<List<FavoriteCharacter>>()

    val updateFavoriteItemId = MutableLiveData<Long>()

    private val mCompositeDisposable = CompositeDisposable()

    fun getAll(offset: Int, limit: Int, nameStartsWith: String? = null,
               orderBy: String = "name", refreshing: Boolean = false) {
        if(refreshing) status.set(Resource.Status.REFRESHING)
        else status.set(Resource.Status.LOADING)

        mCompositeDisposable += mRepository
            .getAll(offset, limit, nameStartsWith, orderBy)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Resource<CharacterApi>>() {

                override fun onNext(data: Resource<CharacterApi>) {
                    val dataStatus = data.status
                    status.set(dataStatus)

                    if(dataStatus == Resource.Status.SUCCESS) {
                        characters.value = convertToCharacter(data.response?.data?.results)
                    }
                }

                override fun onError(e: Throwable) {
                    status.set(Resource.Status.ERROR)
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
                favoriteCharacters.value = favoriteCharactersResult
            },
            {
                Log.e(TAG, "Ocorreu um erro ao recuperar a lista de favoritos local.", it)
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