package com.example.military_service

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentsAdapterMilitarys(
    private var students: List<Student>,
    private val messageClickListener: OnMessageClickListener
) : RecyclerView.Adapter<StudentsAdapterMilitarys.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textName)
        val infoTextView: TextView = itemView.findViewById(R.id.textInfo)
        val messageButton: ImageButton = itemView.findViewById(R.id.messageButton)

        init {
            messageButton.setOnClickListener {
                val student = students[adapterPosition]
                messageClickListener.onMessageClick(student.id.toString(), student.name)
            }
        }
    }

    interface OnMessageClickListener {
        fun onMessageClick(studentId: String, studentName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.nameTextView.text = student.name
        holder.infoTextView.text = student.info
    }

    override fun getItemCount(): Int {
        return students.size
    }

    fun setStudents(studentsList: List<Student>) {
        students = studentsList
        notifyDataSetChanged()
    }

    fun getStudents(): List<Student> {
        return students
    }
}
