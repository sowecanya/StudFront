package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class CabinetActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_cabinet)


        // Проверка, вошел ли пользователь в систему
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Если пользователь не вошел в систему, перенаправляем на экран авторизации
            val intent = Intent(this, Authenfication::class.java)
            startActivity(intent)
            finish() // Закрываем MainActivity, чтобы пользователь не мог вернуться назад
        } else {
            setContentView(R.layout.activity_cabinet)
        }

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonStudents = findViewById<Button>(R.id.buttonStudents)
        val buttonOtchet = findViewById<Button>(R.id.buttonOtchet)



        buttonBack.setOnClickListener {
            val intent = Intent(this, Authenfication::class.java)
            startActivity(intent)
        }
    }
}
