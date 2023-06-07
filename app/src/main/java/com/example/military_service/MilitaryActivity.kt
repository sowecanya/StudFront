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


        setContentView(R.layout.activity_military)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonStudents = findViewById<Button>(R.id.buttonStudents)

        buttonBack.setOnClickListener {
            val intent = Intent(this, Authenfication::class.java)
            startActivity(intent)
            finish()
        }
        buttonStudents.setOnClickListener {
            val intent = Intent(this, StudentsListMilitary::class.java)
            startActivity(intent)
            finish()
        }
    }
}