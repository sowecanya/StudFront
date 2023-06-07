package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Authenfication : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_authenfication)

        // Инициализация FirebaseAuth и FirebaseFirestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Аутентификация пользователя
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        // Успешная аутентификация

                        Log.d("Auth", email + password)
                        getUserData(auth.currentUser?.uid ?: "Nothing")
                    }
                    .addOnFailureListener { exception ->
                        // Ошибка аутентификации
                        Toast.makeText(this, "Ошибка: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
            }
        }
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun getUserData(uid: String) {
        val studentsCollection = db.collection("students").document(uid)
        studentsCollection.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    if (userData != null && userData.containsKey("role")) {
                        when (userData["role"] as String) {
                            "militarys" -> {
                                val intent = Intent(this, MilitaryActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            "student" -> {
                                // Обработка для роли "student"
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            "teacher" -> {
                                // Обработка для роли "teacher"
                                val intent = Intent(this, CabinetActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                // Некорректная роль
                                Toast.makeText(this, "Недостаточно прав доступа", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Роль не указана", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Ошибка: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
