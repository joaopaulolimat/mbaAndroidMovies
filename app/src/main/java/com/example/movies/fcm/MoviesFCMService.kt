package com.example.movies.fcm

import com.example.movies.R
import com.example.movies.utils.NotificationUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MoviesFCMService: FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        val title = p0.data["title"] ?: p0.notification?.title ?:
        this.getString(R.string.app_name)
        val message = p0.data["message"] ?: p0.notification?.body ?: ""
        val channelID = this.getString(R.string.default_notification_channel_id)
        val channelName = this.getString(R.string.default_notification_channel_name)
        val channelDescription =
            this.getString(R.string.default_notification_channel_description)
        val notifChannel = NotificationUtils.NotifChannel(channelID, channelName, channelDescription)
        NotificationUtils.notificationSimple(this, title, message, notifChannel)
    }
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}