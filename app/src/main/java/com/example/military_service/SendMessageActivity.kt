package com.example.military_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SendMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        val studentName = intent.getStringExtra("studentName")
        val studentId = intent.getStringExtra("studentId")

        val fragment = SendMessageFragment.newInstance(studentName, studentId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

}
