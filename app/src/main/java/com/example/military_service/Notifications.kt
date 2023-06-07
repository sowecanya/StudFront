package com.example.military_service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Notifications : AppCompatActivity() {
    private lateinit var newsAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // Инициализируйте RecyclerView и установите LayoutManager
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализируйте адаптер и установите его в RecyclerView
        newsAdapter = NotificationsAdapter(this, ArrayList())
        recyclerView.adapter = newsAdapter
        val buttonBackMessages = findViewById<ImageButton>(R.id.buttonBackNotifications)
        buttonBackMessages.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Загрузите данные новостей
        loadNewsItems()
    }

    // Ваш метод для получения списка новостей
    private fun loadNewsItems() {
        val firestore = FirebaseFirestore.getInstance()
        val newsCollection = firestore.collection("news")

        newsCollection.get().addOnSuccessListener { querySnapshot ->
            val newsItems = mutableListOf<News>()
            for (document in querySnapshot.documents) {
                val title = document.getString("Title") ?: ""
                val image = document.getString("image") ?: ""
                val link = document.getString("link") ?: ""
                val newsItem = News(title, image, link)
                newsItems.add(newsItem)
            }

            // Передайте список newsItems в адаптер и обновите RecyclerView
            newsAdapter.setData(newsItems)
        }.addOnFailureListener { exception ->
            // Обработайте ошибку при загрузке данных
            Log.e("NotificationsActivity", "Error loading news items", exception)
        }
    }
}
