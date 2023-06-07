package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {
    // Здесь можно задать список дат призыва и медкомиссии
    private val callUpDates = listOf("2023-07-01", "2023-07-02", "2023-07-03", "2023-07-04", "2023-07-05", "2023-07-06",
        "2023-07-08", "2023-07-09","2023-07-10")
    private val medicalExamDates = listOf("2023-06-25", "2023-06-26", "2023-06-27", "2023-06-28", "2023-06-29", "2023-06-30")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val buttonBackSettings = findViewById<ImageButton>(R.id.buttonBackCalendar)

        buttonBackSettings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val selectedDate = formatDate(calendar.time)

            if (callUpDates.contains(selectedDate)) {
                Toast.makeText(this, "День призыва", Toast.LENGTH_SHORT).show()
                // Выделение дня призыва в календаре
                calendarView.date = calendar.timeInMillis
            }

            if (medicalExamDates.contains(selectedDate)) {
                Toast.makeText(this, "День медкомиссии", Toast.LENGTH_SHORT).show()
                // Выделение дня медкомиссии в календаре
                calendarView.date = calendar.timeInMillis
            }
        }
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }
}

