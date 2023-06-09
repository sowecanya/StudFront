package com.example.military_service

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class StudentsList : AppCompatActivity(), StudentsAdapter.OnMessageClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentsAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var editSearch: EditText
    private lateinit var buttonSearch: ImageButton
    private lateinit var buttonBackList: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)

        recyclerView = findViewById(R.id.recyclerViewStudents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentsAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        editSearch = findViewById(R.id.editSearch)
        buttonSearch = findViewById(R.id.buttonSearch)
        buttonBackList = findViewById(R.id.buttonBackList)

        buttonSearch.setOnClickListener {
            val searchQuery = editSearch.text.toString().trim()
            searchStudents(searchQuery)
        }

        buttonBackList.setOnClickListener {
            val intent = Intent(this, CabinetActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Инициализация Firestore
        db = FirebaseFirestore.getInstance()

        // Получение списка студентов из базы данных
        getStudentsFromFirestore()
    }

    override fun onMessageClick(studentId: String, studentName: String) {
        val fragment = SendMessageFragment.newInstance(studentName, studentId)
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
                    val info = "$university | Группа: $group | $course Курс"
                    val id = document.getString("uid").toString()
                    val student = Student(name ?: "", info, id)
                    studentsList.add(student)
                }
                adapter.setStudents(studentsList)
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки при получении данных из Firestore
            }
    }
}
