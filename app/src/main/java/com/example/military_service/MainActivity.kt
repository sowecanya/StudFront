package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.military_service.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userNameText: TextView
    private lateinit var userGroupText: TextView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Проверка, вошел ли пользователь в систему
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Если пользователь не вошел в систему, перенаправляем на экран авторизации
            val intent = Intent(this, Authenfication::class.java)
            startActivity(intent)
            finish() // Закрываем MainActivity, чтобы пользователь не мог вернуться назад
        } else {
            val userUid = currentUser.uid
            val userRef = db.collection("students").document(userUid)
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userData = documentSnapshot.data
                        if (userData != null) {
                            val role = userData["role"] as? String
                            handleUserRole(role)
                        } else {
                            Toast.makeText(this, "Данные пользователя не найдены", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Ошибка получения данных пользователя: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

        setContentView(binding.root)

        userNameText = findViewById(R.id.user_name_text)
        userGroupText = findViewById(R.id.user_group_text)

        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = db.collection("students").document(userUid!!)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    if (userData != null) {
                        val firstName = userData["name"] as? String ?: ""
                        val lastName = userData["second_name"] as? String ?: ""
                        val group = userData["group"] as? String ?: ""
                        val fullName = "$lastName $firstName"

                        // Установка полного имени пользователя в поле user_name_text
                        userNameText.text = fullName
                        userGroupText.text = group
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Ошибка: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        val buttonMain = findViewById<Button>(R.id.buttonMainInfo)
        val buttonMilitary = findViewById<Button>(R.id.buttonMilitaryInfo)
        val buttonAdditional = findViewById<Button>(R.id.buttonMore)

        buttonMain.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.place_holder, MainInfoFragment.newInstance())
                .commit()
        }

        buttonMilitary.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.place_holder, MilitaryInfoFragment.newInstance())
                .commit()
        }

        buttonAdditional.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.place_holder, AdditionalInfoFragment.newInstance())
                .commit()
        }

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonNotifications = findViewById<ImageButton>(R.id.buttonNotifications)
        val buttonSettings = findViewById<ImageButton>(R.id.buttonSettings)
        val buttonMessages = findViewById<ImageButton>(R.id.buttonMessages)

        buttonBack.setOnClickListener {
            val intent = Intent(this@MainActivity, Authenfication::class.java)
            startActivity(intent)
        }

        buttonNotifications.setOnClickListener {
            val intent = Intent(this@MainActivity, Messages::class.java)
            startActivity(intent)
        }

        buttonSettings.setOnClickListener {
            val intent = Intent(this@MainActivity, Settings::class.java)
            startActivity(intent)
        }

        buttonMessages.setOnClickListener {
            val intent = Intent(this@MainActivity, Notifications::class.java)
            startActivity(intent)
        }
    }



    private fun handleUserRole(role: String?) {
        when (role) {
            "militarys" -> {
                val intent = Intent(this, MilitaryActivity::class.java)
                startActivity(intent)
                finish()
            }
            "student" -> {
            }
            "teacher" -> {
                val intent = Intent(this, CabinetActivity::class.java)
                startActivity(intent)
                finish()
            }
            else -> {
                Toast.makeText(this, "Недостаточно прав доступа", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
