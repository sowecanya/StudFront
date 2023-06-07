package com.example.military_service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SendMessageFragment : Fragment() {

    private lateinit var editMessageText: EditText
    private lateinit var buttonSendMessage: Button
    private lateinit var buttonClose: ImageButton

    private var studentName: String? = null
    private var studentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentName = it.getString(ARG_STUDENT_NAME)
            studentId = it.getString(ARG_STUDENT_ID)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_send_message, container, false)
        editMessageText = view.findViewById(R.id.editMessageText)
        buttonSendMessage = view.findViewById(R.id.buttonSendMessage)
        buttonClose = view.findViewById(R.id.buttonClose)

        buttonSendMessage.setOnClickListener {
            val messageText = editMessageText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // Отправить сообщение с параметрами messageId, timestamp, senderUid, recipientUid, messageText
                sendMessage(messageText)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        buttonClose.setOnClickListener {
            // Закрыть фрагмент
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun sendMessage(messageText: String) {
        val messageId = generateMessageId()
        val timestamp = System.currentTimeMillis()
        val senderUid = getCurrentUserUid()
        val recipientUid = getRecipientUid()
        val formattedTimestamp = formatTimestamp(timestamp)
        Log.d("RECIPIENT_UID", recipientUid)


        val message = hashMapOf(
            "formattedTimestamp" to formattedTimestamp,
            "senderUid" to senderUid,
            "recipientUid" to recipientUid,
            "messageText" to messageText
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("messages")
            .document(messageId)
            .set(message)
            .addOnSuccessListener {
                // Успешное добавление сообщения в базу данных
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки при добавлении сообщения в базу данных
            }
    }

    private fun generateMessageId(): String {
        // Генерация зашифрованного идентификатора сообщения
        return UUID.randomUUID().toString()
    }

    private fun getCurrentUserUid(): String {
        // или получить идентификатор пользователя из сессии
        // Возвращайте идентификатор текущего пользователя в виде строки
        return FirebaseAuth.getInstance().currentUser?.uid.toString()
    }

    private fun getRecipientUid(): String {
        return studentId ?: ""
    }
    private fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val dateTime = Date(timestamp)
        return dateFormat.format(dateTime)
    }



    companion object {
        private const val ARG_STUDENT_NAME = "studentName"
        private const val ARG_STUDENT_ID = "studentId"

        fun newInstance(studentName: String?, studentId: String?): SendMessageFragment {
            val fragment = SendMessageFragment()
            val args = Bundle()
            args.putString(ARG_STUDENT_NAME, studentName)
            args.putString(ARG_STUDENT_ID, studentId)
            fragment.arguments = args
            return fragment
        }

    }
}
