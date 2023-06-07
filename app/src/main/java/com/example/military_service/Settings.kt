package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackSettings = findViewById<ImageButton>(R.id.buttonBackSettings)
        val buttonNotice = findViewById<Button>(R.id.buttonNotice)
        val buttonCalendar = findViewById<Button>(R.id.buttonCalendar)
        val buttonAbout = findViewById<Button>(R.id.buttonAbout)
        val buttonFeedback = findViewById<Button>(R.id.buttonFeedback)

        buttonBackSettings.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonNotice.setOnClickListener {
            val intent = Intent(this, NoticeActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonFeedback.setOnClickListener {
            val intent = Intent(this, Messages::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}