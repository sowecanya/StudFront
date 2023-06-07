package com.example.military_service

import java.util.Date

data class Message(
    val messageId: String = "",
    val messageText: String = "",
    val senderUid: String = "",
    val recipientUid: String = "",
    val timestamp: String

)

