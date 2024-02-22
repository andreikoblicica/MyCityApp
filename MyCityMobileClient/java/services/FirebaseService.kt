package com.example.communityappmobile.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.communityappmobile.R
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.models.alerts.Alert
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.issue.Issue
import com.example.communityappmobile.models.issue.Message
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.RemoteMessage.Notification
import com.google.firebase.messaging.RemoteMessage.PRIORITY_HIGH
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        if(remoteMessage.from=="/topics/"+User.username){
            remoteMessage.notification?.let { showNewMessage(it) }
        }
        else {
            if(remoteMessage.from=="/topics/community_app_notification"){
                remoteMessage.notification?.let { showAlert(it) }
            }
        }

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }



    private fun showNewMessage(notification: Notification) {
        val message=Gson().fromJson(notification.body, Message::class.java)

        var issue: Issue
        val serverAPI= ServerAPI.createApi()
        serverAPI.getIssueById(message.issueId).enqueue(object:
            Callback<Issue> {
            override fun onResponse(
                call: Call<Issue>,
                response: Response<Issue>
            ) {
                issue = response.body() as Issue

                val bundle = Bundle().apply {
                    putSerializable("issue", issue)
                }

                val pendingIntent = NavDeepLinkBuilder(applicationContext)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.navigation_issue_details)
                    .setArguments(bundle)
                    .setComponentName(HomeActivity::class.java)
                    .createPendingIntent()

                val channelId = "fcm_default_channel"
                val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("New message from "+message.sourceName)
                    .setContentText(message.message)
                    .setAutoCancel(true)
                    .setPriority(PRIORITY_HIGH)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_MAX,
                    )
                    notificationManager.createNotificationChannel(channel)
                }

                val notificationId = 0
                notificationManager.notify(notificationId, notificationBuilder.build())
            }

            override fun onFailure(call: Call<Issue>, t: Throwable) {
                Log.e("GET_FAIL", t.toString())
            }


        })


    }

    private fun showAlert(notification: Notification) {

        val alert=Gson().fromJson(notification.body, Alert::class.java)

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_alerts)
            .setComponentName(HomeActivity::class.java)
            .createPendingIntent()

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title)
            .setContentText(alert.description)
            .setPriority(PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_MAX,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}