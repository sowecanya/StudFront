package com.example.military_service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AdditionalInfoFragment : Fragment() {
    private lateinit var userAddressText: TextView
    private lateinit var userAddressFactText: TextView
    private lateinit var userYearText: TextView

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_additional_info, container, false)

        userAddressText = view.findViewById(R.id.Address)
        userAddressFactText = view.findViewById(R.id.AddressFact)
        userYearText = view.findViewById(R.id.Year)


        db = FirebaseFirestore.getInstance()

        // Получаем данные из базы данных
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = db.collection("students").document(userUid!!)
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    if (userData != null) {
                        val address = userData["address"] as? String ?: ""
                        val address_fact = userData["address_fact"] as? String ?: ""
                        val year_end = userData["year_end"] as? String ?: ""

                        // Устанавливаем значения в TextView
                        userAddressText.text = address
                        userAddressFactText.text = address_fact
                        userYearText.text = year_end
                    }
                }
            }
            .addOnFailureListener {
            }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = AdditionalInfoFragment()
    }
}