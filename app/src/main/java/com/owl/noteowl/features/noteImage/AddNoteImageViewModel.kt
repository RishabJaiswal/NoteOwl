package com.owl.noteowl.features.noteImage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owl.noteowl.data.features.images.models.Image
import com.owl.noteowl.data.features.images.network.ImageApiManager
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.extensions.asLiveData
import com.owl.noteowl.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNoteImageViewModel(private val noteId: Int) : ViewModel() {
    private val noteDao = NoteDao()
    val noteLiveData = noteDao.getNote(noteId)?.asLiveData()
    private val imageApiManager by lazy { ImageApiManager() }
    val imagesLiveData by lazy { MutableLiveData<List<Image>>() }

    class Factory(val noteId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNoteImageViewModel::class.java)) {
                return AddNoteImageViewModel(noteId) as T
            } else {
                throw IllegalArgumentException("Not a valid view model")
            }
        }
    }

    fun getImages() {
        imageApiManager.getImages(12, 1)
            .enqueue(object : Callback<List<Image>> {
                override fun onResponse(call: Call<List<Image>>, response: Response<List<Image>>) {
                    response.body()?.let { images ->
                        imagesLiveData.value = images
                    }
                }

                override fun onFailure(call: Call<List<Image>>, t: Throwable) {

                }
            })
    }

    fun saveImage(url: String) {
        noteDao.saveImage(noteId, url)
    }

    fun saveNote() {
        noteDao.saveStatus(noteId, Constants.NoteStatus().SAVED)
    }
}