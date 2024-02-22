package com.example.communityappmobile.services.ui.issues

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File


class ReportIssueViewModel : ViewModel() {
    private val title = MutableLiveData<String>()
    private val description = MutableLiveData<String>()
    private val type=MutableLiveData<String>()
    private val photoFile = MutableLiveData<File?>()
    private val coordinates=MutableLiveData<String>()

    private val issueTypes = arrayListOf(
        "Power Outage",
        "Water Outage",
        "Streetlight Outage",
        "Road Issue",
        "Garbage Issue",
        "Flood",
        "Fallen Tree",
        "Park Problem",
        "Abandoned Vehicle",
        "Damaged Personal Property",
        "Damaged Public Property",
        "Other"
    )


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

    fun setCoordinates(value: String) {
        coordinates.value = value
    }

    fun getCoordinates(): LiveData<String> {
        return coordinates
    }

    fun getIssueTypes():ArrayList<String>{
        return issueTypes
    }



}