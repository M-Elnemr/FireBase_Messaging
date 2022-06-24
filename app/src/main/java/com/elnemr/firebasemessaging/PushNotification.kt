package com.elnemr.firebasemessaging

data class PushNotification(
    val data: NotificationData,
    val to: String
)