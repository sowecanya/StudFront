package com.example.military_service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MilitaryInfoFragment : Fragment() {
    private lateinit var userlast_comText: TextView
    private lateinit var usernext_comText: TextView
    private lateinit var userCategoryText: TextView
    private lateinit var userProffessionText: TextView
    private lateinit var userOtsrochkaText: TextView
    private lateinit var userDateStartText: TextView

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_military_info, container, false)

        userlast_comText = view.findViewById(R.id.last_com)
        usernext_comText = view.findViewById(R.id.next_com)
        userCategoryText = view.findViewById(R.id.Category)
        userProffessionText = view.findViewById(R.id.Proffession)
        userOtsrochkaText = view.findViewById(R.id.Otsrochka)
        userDateStartText = view.findViewById(R.id.DateStart)


        db = FirebaseFirestore.getInstance()

        // Получаем данные из базы данных
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = db.collection("students").document(userUid!!)
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    if (userData != null) {
                        val last_com = userData["last_com"] as? String ?: ""
                        val next_com = userData["next_com"] as? String ?: ""
                        val Category = userData["category"] as? String ?: ""
                        val Proffession = userData["proffession"] as? String ?: ""
                        val Otsrochka = userData["otsrochka"] as? String ?: ""
                        val DateStart = userData["datestart"] as? String ?: ""

                        // Устанавливаем значения в TextView
                        userlast_comText.text = last_com
                        usernext_comText.text = next_com
                        userCategoryText.text = Category
                        userProffessionText.text = Proffession
                        userOtsrochkaText.text = Otsrochka
                        userDateStartText.text = DateStart
                    }
                }
            }
            .addOnFailureListener {
            }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = MilitaryInfoFragment()
    }
}