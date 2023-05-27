package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Messages : AppCompatActivity() {

    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMessages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messagesAdapter = MessagesAdapter(currentUid = currentUserUid!!) // Замените "your_current_uid" на актуальный UID пользователя
        recyclerView.adapter = messagesAdapter
        val buttonBackMessages = findViewById<ImageButton>(R.id.buttonBackMessages)
        buttonBackMessages.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fetchMessages()
    }

    private fun fetchMessages() {
        val db = FirebaseFirestore.getInstance()
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        db.collection("messages")
            .get()
            .addOnSuccessListener { documents ->
                val messages = mutableListOf<Message>()
                for (document in documents) {
                    val messageId = document.id
                    val messageRecipientUid = document.getString("recipientUid")
                    if (messageRecipientUid == currentUserUid) {
                        val messageText = document.getString("messageText") ?: ""
                        val senderUid = document.getString("senderUid") ?: ""
                        val recipientUid = document.getString("recipientUid") ?: ""
                        val timestamp = document.getDate("timestamp")
                        val message = Message(messageId, messageText, senderUid, recipientUid,
                            timestamp!!
                        )
                        messages.add(message)
                    }
                }
                messagesAdapter.updateMessages(messages)
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки при получении сообщений из базы данных
            }
    }
}
