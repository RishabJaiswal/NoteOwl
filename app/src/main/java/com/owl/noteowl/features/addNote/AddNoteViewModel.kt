package com.owl.noteowl.features.addNote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.owl.noteowl.data.features.notes.models.Label
import io.realm.RealmList

class AddNoteViewModel : ViewModel() {
    var labelsLiveData = MutableLiveData<RealmList<Label>>().apply {
        value = RealmList()
    }

    //saving label
    fun saveLabel(name: String?, color: Int?) {
        if (name != null && color != null) {
            labelsLiveData.apply {
                value?.add(Label().apply {
                    title = name
                    colorHex = color
                })
                value = value
            }
        }
    }

    //removing label
    fun removeLabel(position: Int) {
        labelsLiveData.apply {
            value?.removeAt(position)
            value = value
        }
    }
}