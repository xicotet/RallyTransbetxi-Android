package com.canolabs.rallytransbetxi.utils

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FcmListenerService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            // Handle the data message here.
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            // Handle the notification message here.
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send the new Instance ID token to your app server.
    }
}