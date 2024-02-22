package com.example.communityappmobile.apis

import com.example.communityappmobile.models.alerts.Alert
import com.example.communityappmobile.models.auth.*
import com.example.communityappmobile.models.elements.Facility
import com.example.communityappmobile.models.events.Event
import com.example.communityappmobile.models.institutions.Institution
import com.example.communityappmobile.models.issue.Issue
import com.example.communityappmobile.models.issue.Message
import com.example.communityappmobile.models.issue.NewIssue
import com.example.communityappmobile.models.news.News
import com.example.communityappmobile.models.user.UserAnalytics
import com.example.communityappmobile.models.user.UserUpdate
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ServerAPI {

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    fun authenticate(@Body body: AuthRequest): Call<AuthResponse>

    @POST("auth/signup")
    @Headers("Content-Type: application/json")
    fun signUp(@Body body: SignUpRequest): Call<MessageResponse>

    @PUT("admin/users")
    @Headers("Content-Type: application/json")
    fun updateUser(@Body body: UserUpdate): Call<UserUpdate>

    @DELETE("admin/users/{id}")
    @Headers("Content-Type: application/json")
    fun deleteUser(@Path("id") id: Long): Call<Void>

    @GET("admin/facilities")
    @Headers("Content-Type: application/json")
    fun getAllFacilities(): Call<ArrayList<Facility>>

    @GET("admin/institutions")
    @Headers("Content-Type: application/json")
    fun getAllInstitutions(): Call<ArrayList<Institution>>

    @GET("admin/alerts")
    @Headers("Content-Type: application/json")
    fun getAllAlerts(): Call<ArrayList<Alert>>

    @POST("admin/issues")
    @Headers("Content-Type: application/json")
    fun createIssue(@Body body: NewIssue): Call<Issue>

    @POST("admin/events")
    @Headers("Content-Type: application/json")
    fun createEvent(@Body body: Event): Call<Event>

    @GET("user/issues/{username}")
    @Headers("Content-Type: application/json")
    fun getIssues(@Path("username") username: String): Call<ArrayList<Issue>>

    @GET("admin/news")
    @Headers("Content-Type: application/json")
    fun getNews(): Call<ArrayList<News>>

    @GET("admin/events/filter/{status}")
    @Headers("Content-Type: application/json")
    fun getEventsByStatus(@Path("status") status: String): Call<ArrayList<Event>>

    @GET("admin/events/userevents/{id}")
    @Headers("Content-Type: application/json")
    fun getEventsByUser(@Path("id") id: Long): Call<ArrayList<Event>>

    @GET("admin/events/favorites/{id}")
    @Headers("Content-Type: application/json")
    fun getFavoriteEventsByUser(@Path("id") id: Long): Call<ArrayList<Event>>

    @DELETE("admin/events/{id}")
    @Headers("Content-Type: application/json")
    fun deleteEvent(@Path("id") id: Long): Call<Void>

    @POST("admin/events/favorites/add/{userId}/{eventId}")
    @Headers("Content-Type: application/json")
    fun addToFavorites(@Path("userId") userId: Long,@Path("eventId") eventId: Long): Call<Void>

    @POST("admin/events/favorites/remove/{userId}/{eventId}")
    @Headers("Content-Type: application/json")
    fun removeFromFavorites(@Path("userId") userId: Long,@Path("eventId") eventId: Long): Call<Void>

    @GET("admin/events/favorites/{userId}/{eventId}")
    @Headers("Content-Type: application/json")
    fun isFavorite(@Path("userId") userId: Long,@Path("eventId") eventId: Long): Call<Boolean>

    @POST("send")
    @Headers("Content-Type: application/json")
    fun sendMessage(@Body body: Message): Call<Void>

    @GET("institution/issue/{id}")
    @Headers("Content-Type: application/json")
    fun getIssueById(@Path("id") id: Long): Call<Issue>

    @GET("user/analytics/{id}")
    @Headers("Content-Type: application/json")
    fun getUserAnalytics(@Path("id") id: Long): Call<UserAnalytics>


    @POST("admin/users/existsusername/{username}")
    @Headers("Content-Type: application/json")
    fun usernameExists(@Path("username") username: String): Call<Boolean>

    @POST("admin/users/existsemail/{email}")
    @Headers("Content-Type: application/json")
    fun emailExists(@Path("email") email: String): Call<Boolean>



    companion object{
        fun createApi():ServerAPI{
            val retrofit = Retrofit.Builder()
                .baseUrl ( "http://192.168.1.131:8081/api/" )
                .addConverterFactory ( GsonConverterFactory.create() )
                .build()
            return retrofit.create(ServerAPI::class.java)
        }
    }
}