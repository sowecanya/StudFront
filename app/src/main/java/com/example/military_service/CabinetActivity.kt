package com.example.military_service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CabinetActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_cabinet)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonStudents = findViewById<Button>(R.id.buttonStudents)
        val buttonOtchet = findViewById<Button>(R.id.buttonOtchet)
        val buttonStats = findViewById<Button>(R.id.buttonStats)

        buttonBack.setOnClickListener {
            val intent = Intent(this, Authenfication::class.java)
            startActivity(intent)
            finish() // Закрываем CabinetActivity, чтобы пользователь не мог вернуться назад
        }

        buttonStudents.setOnClickListener {
            val intent = Intent(this, StudentsList::class.java)
            startActivity(intent)
            finish()
        }
        buttonStats.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonOtchet.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                createExcelFile()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun createExcelFile() {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Отчет")

        // Создание заголовков колонок
        val headerRow: Row = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Имя студента")
        headerRow.createCell(1).setCellValue("Группа")
        headerRow.createCell(2).setCellValue("Статус")
        headerRow.createCell(3).setCellValue("Дата следующей явки")

        // Получение данных студентов из Firestore
        getStudentsData().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val students = task.result
                // Заполнение данными
                for ((index, student) in students.withIndex()) {
                    val row: Row = sheet.createRow(index + 1)
                    row.createCell(0).setCellValue(student.name)
                    row.createCell(1).setCellValue(student.group)
                    row.createCell(2).setCellValue(student.status)
                    row.createCell(3).setCellValue(student.nextComDate)
                }

                // Сохранение файла и загрузка на устройство
                try {
                    val filePath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                    val file = File(filePath, "отчет.xlsx")
                    val fileOutputStream = FileOutputStream(file)
                    workbook.write(fileOutputStream)
                    fileOutputStream.close()
                    workbook.close()
                    // Загрузка файла на устройство
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(
                        FileProvider.getUriForFile(
                            this,
                            "com.example.military_service.fileprovider",
                            file
                        ),
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    )
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(intent)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                // Обработка ошибки при получении данных из Firestore
            }
        }
    }

    private fun getStudentsData(): Task<List<StudentOtchet>> {
        val students = mutableListOf<StudentOtchet>()

        val db = FirebaseFirestore.getInstance()
        val query = db.collection("students")
            .whereEqualTo("role", "student")
            .get()

        return query.continueWith { task ->
            if (task.isSuccessful) {
                val querySnapshot = task.result
                for (document in querySnapshot.documents) {
                    val name = document.getString("name")
                    val group = document.getString("group")
                    val status = document.getString("status")
                    val nextCom = document.getString("next_com")
                    if (name != null && group != null && status != null && nextCom != null) {
                        students.add(StudentOtchet(name, group, status, nextCom))
                    }
                }
            } else {
                // Обработка ошибки при получении данных из Firestore
            }

            // Возвращаем список студентов
            students
        }
    }
}
