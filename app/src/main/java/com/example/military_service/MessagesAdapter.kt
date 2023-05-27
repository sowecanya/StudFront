package com.example.military_service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MessagesAdapter(private val currentUid: String) :
    RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    private val messageList: MutableList<Message> = mutableListOf()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessage: TextView = itemView.findViewById(R.id.textMessage)
        private val textTime: TextView = itemView.findViewById(R.id.textTime)
        private val textNameSender: TextView = itemView.findViewById(R.id.textNameSender)

        fun bind(message: Message) {
            textMessage.text = message.messageText
            textTime.text = formatDate(message.timestamp)

            val senderUid = message.senderUid

            // Получаем данные отправителя из коллекции "students" по senderUid
            val db = FirebaseFirestore.getInstance()
            val senderRef = db.collection("students").document(senderUid)
            senderRef.get().addOnSuccessListener { senderSnapshot ->
                if (senderSnapshot.exists()) {
                    val senderData = senderSnapshot.data
                    if (senderData != null) {
                        val senderName = senderData["name"] as? String
                        textNameSender.text = senderName
                    }
                }
            }
        }

        private fun formatDate(timestamp: Date): String {
            val dateFormat = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
            return dateFormat.format(timestamp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_message,
            parent,
            false
        )
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun updateMessages(messages: List<Message>) {
        messageList.clear()
        messageList.addAll(messages)
        notifyDataSetChanged()
    }
}

