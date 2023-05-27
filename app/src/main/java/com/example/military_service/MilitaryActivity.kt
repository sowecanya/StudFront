package com.example.military_service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class MilitaryActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()


        // Проверка, вошел ли пользователь в систему
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Если пользователь не вошел в систему, перенаправляем на экран авторизации
            val intent = Intent(this, Authenfication::class.java)
            startActivity(intent)
            finish() // Закрываем MainActivity, чтобы пользователь не мог вернуться назад
        } else {
            setContentView(R.layout.activity_military)
        }

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonStudents = findViewById<Button>(R.id.buttonStudents)

        buttonBack.setOnClickListener {
            val intent = Intent(this, Authenfication::class.java)
            startActivity(intent)
        }
        buttonStudents.setOnClickListener {
            val intent = Intent(this, StudentsList::class.java)
            startActivity(intent)
        }
    }
}