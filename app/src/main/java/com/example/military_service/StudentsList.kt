package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class StudentsList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var editSearch: EditText
    private lateinit var buttonSearch: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)

        recyclerView = findViewById(R.id.recyclerViewStudents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter { studentId, studentName ->
            // Вызывается при нажатии кнопки сообщения
            openSendMessageFragment(studentId, studentName)
        }

        recyclerView.adapter = adapter

        editSearch = findViewById(R.id.editSearch)
        buttonSearch = findViewById(R.id.buttonSearch)

        buttonSearch.setOnClickListener {
            val searchQuery = editSearch.text.toString().trim()
            searchStudents(searchQuery)
        }

        // Инициализация Firestore
        db = FirebaseFirestore.getInstance()

        // Получение списка студентов из базы данных
        getStudentsFromFirestore()
    }
    private fun openSendMessageFragment(recipientId: String, recipientName: String) {
        val fragment = SendMessageFragment.newInstance(recipientId, recipientName)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun searchStudents(query: String) {
        val filteredList = mutableListOf<Student>()

        // Проход по списку студентов и проверка соответствия критериям поиска
        for (student in adapter.getStudents()) {
            if (student.name.contains(query, ignoreCase = true)) {
                filteredList.add(student)
            }
        }

        // Обновление списка студентов в адаптере
        adapter.setStudents(filteredList)
    }


    private fun getStudentsFromFirestore() {
        db.collection("students")
            .whereEqualTo("role", "student")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val studentsList = mutableListOf<Student>()
                for (document in querySnapshot.documents) {
                    val name = document.getString("name")
                    val group = document.getString("group")
                    val university = document.getString("university")
                    val course = document.getString("course")
                    val info = "$university | $group | $course Курс"
                    val student = Student(name ?: "", info ?: "")
                    studentsList.add(student)
                }
                adapter.setStudents(studentsList)
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки при получении данных из Firestore
            }
    }
}