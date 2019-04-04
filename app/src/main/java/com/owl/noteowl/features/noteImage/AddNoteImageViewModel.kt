package com.owl.noteowl.features.noteImage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owl.noteowl.data.Result
import com.owl.noteowl.data.features.images.models.Image
import com.owl.noteowl.data.features.images.models.ImageSearchResult
import com.owl.noteowl.data.features.images.network.ImageApiManager
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.asLiveData
import com.owl.noteowl.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNoteImageViewModel(private val noteId: Int) : ViewModel() {
    private val noteDao = NoteDao()
    val noteLiveData = noteDao.getNote(noteId)?.asLiveData()
    private val imageApiManager by lazy { ImageApiManager() }
    private val images: ArrayList<Image> = arrayListOf()
    val imagesLiveData by lazy { MutableLiveData<Result<List<Image>>>() }
    var currentSearchQuery: String? = null

    //params for pagination
    private val PAGE_SIZE = 10
    private var pageNo = 0
    private var isLastPage: Boolean = false

    class Factory(val noteId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNoteImageViewModel::class.java)) {
                return AddNoteImageViewModel(noteId) as T
            } else {
                throw IllegalArgumentException("Not a valid view model")
            }
        }
    }

    fun getRandomImages() {
        imagesLiveData.value = Result.loading(true)
        imageApiManager.getRandomImages(PAGE_SIZE, ++pageNo)
            .enqueue(object : Callback<List<Image>> {

                override fun onResponse(call: Call<List<Image>>, response: Response<List<Image>>) {
                    response.body()?.let { images ->
                        addImages(images)
                        if (images.size < PAGE_SIZE) {
                            isLastPage = true
                        }
                    }
                }

                override fun onFailure(call: Call<List<Image>>, t: Throwable) {
                    imagesLiveData.value = Result.error(t)
                }
            })
    }

    fun searchImages(query: String) {
        currentSearchQuery = query
        imagesLiveData.value = Result.loading(true)
        imageApiManager.searchImages(query, PAGE_SIZE, ++pageNo)
            .enqueue(object : Callback<ImageSearchResult> {

                override fun onResponse(call: Call<ImageSearchResult>, response: Response<ImageSearchResult>) {
                    response.body()?.images?.let { images ->
                        addImages(images)
                        if (images.size < PAGE_SIZE) {
                            isLastPage = true
                        }
                    }
                }

                override fun onFailure(call: Call<ImageSearchResult>, t: Throwable) {
                    imagesLiveData.value = Result.error(t)
                }
            })
    }

    fun saveImage(url: String) {
        noteDao.saveImage(noteId, url)
    }

    fun saveNote() {
        noteDao.saveStatus(noteId, Constants.NoteStatus().SAVED)
    }

    fun getNote(): Note? {
        return noteLiveData?.value
    }

    fun isNoteImagePresent(): Boolean {
        return !getNote()?.imageUrl.isNullOrEmpty()
    }

    fun canLoadMoreImages(): Boolean {
        return !isLastPage && !(imagesLiveData.value is Result.Progress)
    }

    fun addImages(images: List<Image>) {
        this.images.addAll(images)
        imagesLiveData.value = Result.success(this.images)
    }

    fun clearImages() {
        this.images.clear()
        pageNo = 0
        imagesLiveData.value = Result.success(this.images)
    }
}