package dev.fsnasser.marvelcharacters.ui.views.main

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fsnasser.marvelcharacters.data.entities.CharacterApi
import dev.fsnasser.marvelcharacters.data.rest.characters.CharactersRepository
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

    val status = ObservableField<Resource.Status>()

    val characters = MutableLiveData<List<Character>>()

    val search = MutableLiveData<String>()

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

    private fun convertToCharacter(results: List<CharacterApi.Data.Result>?) : List<Character> {
        val characters = ArrayList<Character>()

        results?.let {
            for(result in it) {
                characters.add(Character(result.id, result.thumbnail.path, result.thumbnail.extension,
                    result.name, false, result.description))
            }
        }

        return characters
    }

    override fun onCleared() {
        super.onCleared()

        if(!mCompositeDisposable.isDisposed) mCompositeDisposable.dispose()
    }

}