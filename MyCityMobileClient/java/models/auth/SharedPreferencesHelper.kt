package com.example.communityappmobile.models.auth

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val PREF_NAME = "CommunityAppPreferences"
    private const val KEY_LOGGED_IN = "isLoggedIn"
    private const val KEY_USER_ID = "userId"
    private const val KEY_USER_NAME = "userName"
    private const val KEY_USER_USERNAME = "userUsername"
    private const val KEY_USER_EMAIL = "userEmail"
    private const val KEY_USER_TOKEN = "userToken"



    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_LOGGED_IN, isLoggedIn).apply()
    }

    fun getUser(context: Context){
        User.id=getSharedPreferences(context).getLong(KEY_USER_ID,0)
        User.username= getSharedPreferences(context).getString(KEY_USER_USERNAME, "").toString()
        User.name=getSharedPreferences(context).getString(KEY_USER_NAME, "").toString()
        User.email=getSharedPreferences(context).getString(KEY_USER_EMAIL, "").toString()
        User.token=getSharedPreferences(context).getString(KEY_USER_TOKEN,"").toString()
    }

    fun setUser(context: Context){
        getSharedPreferences(context).edit().putLong(KEY_USER_ID, User.id).apply()
        getSharedPreferences(context).edit().putString(KEY_USER_USERNAME, User.username).apply()
        getSharedPreferences(context).edit().putString(KEY_USER_NAME,  User.name).apply()
        getSharedPreferences(context).edit().putString(KEY_USER_EMAIL,  User.email).apply()
        getSharedPreferences(context).edit().putString(KEY_USER_TOKEN, User.token).apply()
    }
}