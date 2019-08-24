package dev.fsnasser.marvelcharacters.ui.views.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fsnasser.marvelcharacters.data.entities.ComicSerieApi
import dev.fsnasser.marvelcharacters.data.rest.comics.ComicsRepository
import dev.fsnasser.marvelcharacters.ui.entities.Character
import dev.fsnasser.marvelcharacters.utils.data.Resource
import dev.fsnasser.marvelcharacters.utils.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharacterDetailViewModel @Inject constructor(
    private val mRepository: ComicsRepository) : ViewModel() {

    val status = ObservableField<Resource.Status>()

    val comics = MutableLiveData<List<Character.ComicSerie>>()

    private val mCompositeDisposable = CompositeDisposable()

    fun getAllFromCharacter(id: Long, limit: Int, noVariants: Boolean = true) {
        status.set(Resource.Status.LOADING)

        mCompositeDisposable += mRepository
            .getAllFromCharacter(id, limit, noVariants)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Resource<ComicSerieApi>>() {

                override fun onNext(data: Resource<ComicSerieApi>) {
                    val dataStatus = data.status
                    status.set(dataStatus)

                    if(dataStatus == Resource.Status.SUCCESS) {
                        comics.value = convertToComicSerie(data.response?.data?.results)
                    }
                }

                override fun onError(e: Throwable) {
                    status.set(Resource.Status.ERROR)
                }

                override fun onComplete() {}
            })
    }

    private fun convertToComicSerie(results: List<ComicSerieApi.Data.Result>?) : List<Character.ComicSerie> {
        val comics = ArrayList<Character.ComicSerie>()
        results?.let {
            for(result in it) {
                comics.add(Character.ComicSerie(result.title, result.thumbnail.path,
                    result.thumbnail.extension))
            }
        }
        return comics
    }

    override fun onCleared() {
        super.onCleared()

        if(!mCompositeDisposable.isDisposed) mCompositeDisposable.dispose()
    }

}