package com.example.communityappmobile.models.elements

class MainMenuOption(imageRes: Int,iconRes: Int, title: String?) {


    private var imageRes = 0

    private var title: String? = null

    init{
        this.imageRes = imageRes

        this.title = title
    }

    fun getImageRes(): Int {
        return imageRes
    }


    fun getTitle(): String? {
        return title
    }
}