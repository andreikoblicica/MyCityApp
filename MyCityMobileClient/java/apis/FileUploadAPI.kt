package com.example.communityappmobile.apis

import com.example.communityappmobile.models.file.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileUploadAPI {

    @Multipart
    @POST("upload")
    fun uploadFile(
        @Part filePart: MultipartBody.Part
    ): Call<UploadResponse>

    companion object {
        fun createApi(): FileUploadAPI {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.131:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(FileUploadAPI::class.java)
        }
    }
}