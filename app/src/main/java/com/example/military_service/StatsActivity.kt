package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class StatsActivity : AppCompatActivity() {

    private lateinit var allCountTextView: TextView
    private lateinit var militarysCountTextView: TextView
    private lateinit var buttonBackStats: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        // Найти TextView
        allCountTextView = findViewById(R.id.allCountTextView)
        militarysCountTextView = findViewById(R.id.militarysCountTextView)

        // Найти кнопку
        buttonBackStats = findViewById(R.id.buttonBackStats)
        buttonBackStats.setOnClickListener {
            onBackPressed()
        }

        // Получение данных из базы данных
        fetchStudentsData()
    }

    private fun fetchStudentsData() {
        val db = FirebaseFirestore.getInstance()

        // Получение общего количества студентов
        db.collection("students")
            .whereEqualTo("role", "student")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val allCount = querySnapshot.size()
                allCountTextView.text = allCount.toString()
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки при получении данных из Firestore
            }

        // Получение количества военнообязанных студентов
        db.collection("students")
            .whereEqualTo("role", "student")
            .whereEqualTo("status", "Военнообязанный")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val militarysCount = querySnapshot.size()
                militarysCountTextView.text = militarysCount.toString()
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки при получении данных из Firestore
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Возврат на экран CabinetActivity
        val intent = Intent(this, CabinetActivity::class.java)
        startActivity(intent)
        finish()
    }
}
