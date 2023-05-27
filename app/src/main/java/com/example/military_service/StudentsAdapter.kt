package com.example.military_service

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class StudentAdapter(private val listener: (String, String) -> Unit) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private val students: MutableList<Student> = mutableListOf()

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textName)
        val infoTextView: TextView = itemView.findViewById(R.id.textInfo)
        val messageButton: ImageButton = itemView.findViewById(R.id.messageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.nameTextView.text = student.name
        holder.infoTextView.text = student.info

        holder.messageButton.setOnClickListener {
            val fragment = SendMessageFragment.newInstance(student.name, student.id)
            val activity = holder.itemView.context as AppCompatActivity
            val studentId = student.id
            val studentName = student.name
            listener.invoke(studentId, studentName)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }

    fun setStudents(studentsList: List<Student>) {
        students.clear()
        students.addAll(studentsList)
        notifyDataSetChanged()
    }

    fun getStudents(): List<Student> {
        return students
    }
}
