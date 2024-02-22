package com.example.communityappmobile.services.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class CreateEventViewModel : ViewModel() {
    private val title = MutableLiveData<String>()
    private val description = MutableLiveData<String>()
    private val type= MutableLiveData<String>()
    private val photoFile = MutableLiveData<File?>()
    private val location= MutableLiveData<String>()


    fun setTitle(value: String) {
        title.value = value
    }

    fun getTitle(): LiveData<String> {
        return title
    }

    fun setDescription(value: String) {
        description.value = value
    }

    fun getDescription(): LiveData<String> {
        return description
    }

    fun setType(value: String) {
        type.value = value
    }

    fun getType(): LiveData<String> {
        return type
    }

    fun setPhoto(value: File?) {
        photoFile.value = value
    }

    fun getPhoto(): LiveData<File?> {
        return photoFile
    }

    fun setLocation(value: String) {
        location.value = value
    }

    fun getLocation(): LiveData<String> {
        return location
    }



}