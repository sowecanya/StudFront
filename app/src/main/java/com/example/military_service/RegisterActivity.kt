package com.example.military_service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    // Объявите объект FirebaseAuth для аутентификации пользователя
    private lateinit var auth: FirebaseAuth

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editStatus: EditText
    private lateinit var editName: EditText
    private lateinit var editUniversity: EditText
    private lateinit var editFaculty: EditText
    private lateinit var editGroup: EditText
    private lateinit var editCourse: EditText
    private lateinit var editBirthday: EditText
    private lateinit var editLastCome: EditText
    private lateinit var editNextCome: EditText
    private lateinit var editCategory: EditText
    private lateinit var editProfession: EditText
    private lateinit var editOtsrochka: EditText
    private lateinit var editDateStart: EditText
    private lateinit var editAddress: EditText
    private lateinit var editAddressFact: EditText
    private lateinit var editYear: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Инициализируйте объект FirebaseAuth
        auth = FirebaseAuth.getInstance()

        editEmail = findViewById(R.id.EditEmail)
        editPassword = findViewById(R.id.EditPassword)
        editStatus = findViewById(R.id.EditStatus)
        editName = findViewById(R.id.EditName)
        editUniversity = findViewById(R.id.EditUniversity)
        editFaculty = findViewById(R.id.EditFaculty)
        editGroup = findViewById(R.id.EditGroup)
        editCourse = findViewById(R.id.EditCourse)
        editBirthday = findViewById(R.id.EditBirthday)
        editLastCome = findViewById(R.id.EditLastCome)
        editNextCome = findViewById(R.id.EditNextCome)
        editCategory = findViewById(R.id.EditCategory)
        editProfession = findViewById(R.id.EditProffession)
        editOtsrochka = findViewById(R.id.EditOtsrochka)
        editDateStart = findViewById(R.id.EditDateStart)
        editAddress = findViewById(R.id.EditAddress)
        editAddressFact = findViewById(R.id.EditAddressFact)
        editYear = findViewById(R.id.EditYear)

        val buttonBackRegister = findViewById<ImageButton>(R.id.buttonBackRegister)
        buttonBackRegister.setOnClickListener {
            val intent = Intent(this, Authenfication::class.java)
            startActivity(intent)
        }
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        buttonRegister.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            // Создайте нового пользователя с помощью аутентификации Firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        val uid = currentUser?.uid

                        // Получите значения из EditText
                        val status = editStatus.text.toString()
                        val name = editName.text.toString()
                        val university = editUniversity.text.toString()
                        val faculty = editFaculty.text.toString()
                        val group = editGroup.text.toString()
                        val course = editCourse.text.toString()
                        val birthday = editBirthday.text.toString()
                        val last_com = editLastCome.text.toString()
                        val next_com = editNextCome.text.toString()
                        val category = editCategory.text.toString()
                        val proffession = editProfession.text.toString()
                        val otsrochka = editOtsrochka.text.toString()
                        val datestart = editDateStart.text.toString()
                        val address = editAddress.text.toString()
                        val address_fact = editAddressFact.text.toString()
                        val year_end = editYear.text.toString()
                        val role = "student"

                        // Создайте объект пользователя с полученными значениями
                        val user = User(
                            uid,
                            status,
                            name,
                            university,
                            faculty,
                            group,
                            course,
                            birthday,
                            last_com,
                            next_com,
                            category,
                            proffession,
                            otsrochka,
                            datestart,
                            address,
                            address_fact,
                            role,
                            year_end
                        )

                        // Получите экземпляр Firestore
                        val db = FirebaseFirestore.getInstance()

                        // Сохраните пользователя в коллекции "users" с использованием его UID в качестве идентификатора документа
                        uid?.let {
                            db.collection("students")
                                .document(it)
                                .set(user)
                                .addOnSuccessListener {
                                    // Успешно сохранено в Firestore
                                    Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, Authenfication::class.java)
                                    startActivity(intent)
                                    finish()
                                    // Переключитесь на другую активность или выполните другие операции
                                }
                                .addOnFailureListener { e ->
                                    // Ошибка при сохранении в Firestore
                                    Toast.makeText(this, "Ошибка регистрации: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        // Ошибка при создании пользователя
                        Toast.makeText(this, "Ошибка регистрации: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    data class User(
        val uid: String?,
        val status: String,
        val name: String,
        val university: String,
        val faculty: String,
        val group: String,
        val course: String,
        val birthday: String,
        val last_com: String,
        val next_com: String,
        val category: String,
        val proffession: String,
        val otsrochka: String,
        val datestart: String,
        val address: String,
        val address_fact: String,
        val role: String,
        val year_end: String
    )
}
