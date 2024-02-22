package com.example.communityappmobile.models.elements

data class Facility(val id: Long,
               val type: String,
               val name: String,
               val description: String,
               val location: String,
               val websiteLink: String,
               val imageUrl: String):java.io.Serializable{

}