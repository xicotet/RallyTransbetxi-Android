package com.canolabs.rallytransbetxi.ui.miscellaneous

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FcmListenerService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM", "Message data payload: ${remoteMessage.data}")
            // Handle the data message here.
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
            // Handle the notification message here.
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send the new Instance ID token to your app server.
    }
}