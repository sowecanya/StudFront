package com.example.military_service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainInfoFragment : Fragment() {
    private lateinit var userStatusText: TextView
    private lateinit var userUniversityText: TextView
    private lateinit var userFacultyText: TextView
    private lateinit var userGroup1Text: TextView
    private lateinit var userCourseText: TextView
    private lateinit var userBirthdayText: TextView

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_info, container, false)


        userStatusText = view.findViewById(R.id.status)
        userUniversityText = view.findViewById(R.id.University)
        userFacultyText = view.findViewById(R.id.Faculty)
        userGroup1Text = view.findViewById(R.id.Group)
        userCourseText = view.findViewById(R.id.Course)
        userBirthdayText = view.findViewById(R.id.Birthday)


        db = FirebaseFirestore.getInstance()

        // Получаем данные из базы данных
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = db.collection("students").document(userUid!!)
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    if (userData != null) {
                        val group = userData["group"] as? String ?: ""
                        val status = userData["status"] as? String ?: ""
                        val university = userData["university"] as? String ?: ""
                        val faculty = userData["faculty"] as? String ?: ""
                        val course = userData["course"] as? String ?: ""
                        val birthday = userData["birthday"] as? String ?: ""

                        // Устанавливаем значения в TextView
                        userStatusText.text = status
                        userUniversityText.text = university
                        userFacultyText.text = faculty
                        userGroup1Text.text = group
                        userCourseText.text = course
                        userBirthdayText.text = birthday
                    }
                }
            }
            .addOnFailureListener {
            }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainInfoFragment()
    }
}