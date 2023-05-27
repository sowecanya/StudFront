package com.example.military_service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class NotificationsAdapter(
    private val context: Context,
    private var newsItems: List<News>
) : RecyclerView.Adapter<NotificationsAdapter.NewsViewHolder>() {

    fun setData(newsItems: List<News>) {
        this.newsItems = newsItems
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textNewsTitle)
        private val imageView: ImageView = itemView.findViewById(R.id.imageNews)

        fun bind(newsItem: News) {
            titleTextView.text = newsItem.title

            val imageView: ImageView = itemView.findViewById(R.id.imageNews)

            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images").child(newsItem.image + ".jpg")

            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Загрузка изображения успешна
                val imageUrl = uri.toString()

                // Используйте любую библиотеку для загрузки изображения или загрузите его вручную
                // Например, с помощью библиотеки Picasso:
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageView)
            }.addOnFailureListener { exception ->
                // Обработка ошибки при загрузке изображения
                Log.e("NotificationsActivity", "Error loading image", exception)
            }

            itemView.setOnClickListener {
                openNewsUrl(newsItem.link)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsItems[position]
        holder.bind(currentItem)
    }

    private fun openNewsUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        context.startActivity(intent)
    }
}
