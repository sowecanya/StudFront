package com.example.military_service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class SendMessageFragment : Fragment() {

    private lateinit var editMessageText: EditText
    private lateinit var buttonSendMessage: Button

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

        buttonSendMessage.setOnClickListener {
            val messageText = editMessageText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // Отправить сообщение с параметрами messageId, timestamp, senderUid, recipientUid, messageText
                sendMessage(messageText)
            }
        }

        return view
    }

    private fun sendMessage(messageText: String) {
        val messageId = generateMessageId() // Генерация уникального идентификатора сообщения
        val timestamp = System.currentTimeMillis() // Получение текущего времени в миллисекундах
        val senderUid = getCurrentUserUid() // Получение идентификатора текущего пользователя (отправителя)
        val recipientUid = getRecipientUid() // Получение идентификатора получателя сообщения

        // Создание объекта сообщения с необходимыми полями
        val message = hashMapOf(
            "timestamp" to timestamp,
            "senderUid" to senderUid,
            "recipientUid" to recipientUid,
            "messageText" to messageText
        )

        // Добавление сообщения в коллекцию "messages"
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
        return studentId.toString()
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
