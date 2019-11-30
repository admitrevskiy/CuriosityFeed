package com.github.admitrevskiy.images.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.admitrevskiy.images.model.InitialLoadStatus
import com.github.admitrevskiy.images.model.Photo
import com.github.admitrevskiy.images.model.rest.NasaResponse
import com.github.admitrevskiy.images.repository.PhotoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class MainViewModel(private val repository: PhotoRepository) : ViewModel() {

    val photosLiveData: MutableLiveData<List<Photo>> = MutableLiveData()
    val initialLoadStatusLiveData: MutableLiveData<InitialLoadStatus> = MutableLiveData()

    private val disposable = CompositeDisposable()
    private val loadedPhotos = ArrayList<Photo>()

    private val tag = "MainViewModel"

    private var page : Int = 0
    private var sol : Int = 1

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    /**
     * Trigger loading if it was not triggered before
     */
    fun onResume() {
        if (initialLoadStatusLiveData.value == null) {
            loadPhotos()
        }
    }

    fun loadPhotos() {
        if (loadedPhotos.isEmpty()) {
            initialLoadStatusLiveData.postValue(InitialLoadStatus.IN_PROGRESS)
        }
        page++
        disposable.add(repository
            .getPhotos(sol, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handleResponse(it) },
                { handleError(it)    }
            ))
    }

    private fun handleError(t: Throwable) {
        Log.w(tag, "Failed to load photos", t)
        when (t) {
            is HttpException ->  {
                when (t.code()) {
                    401         -> {} // Unauthorized (Bad key)
                    429         -> {} // Too many requests (Slow down, swipe master)
                    in 400..499 -> {} // Client error
                    in 500..599 -> {} // Server error
                    else        -> {} // WTF?
                }
            }
        }
        if (initialLoadStatusLiveData.value != InitialLoadStatus.LOADED) {
            initialLoadStatusLiveData.postValue(InitialLoadStatus.ERROR)
        }
    }

    /**
     * Handles response:
     *
     * - Filters out photos without source ur
     * - Asks for new data if there are no valid photos
     * - Posts photos if data is valid
     */
    private fun handleResponse(response: NasaResponse) {
        response.photos.apply {
            if (size > 0) {
                postPhotos(this)
            } else {
                loadNextSol()
            }
        }
    }

    /**
     * Looks like there are no more photos for current sol. Let's check the next one!
     */
    private fun loadNextSol() {
        sol++
        page = 0 // Reset page
        loadPhotos()
    }

    /**
     * Posts new photos loaded from server
     */
    private fun postPhotos(loaded: List<Photo>) {
        loadedPhotos.addAll(loaded)
        photosLiveData.postValue(loadedPhotos)
        if (initialLoadStatusLiveData.value != InitialLoadStatus.LOADED) {
            initialLoadStatusLiveData.postValue(InitialLoadStatus.LOADED)
        }
    }
}
