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
    private lateinit var userProffessionText: TextView
    private lateinit var userDateStartText: TextView

    private lateinit var text_status_medcomission: TextView
    private lateinit var text_date_medcomission: TextView
    private lateinit var text_height: TextView
    private lateinit var userCategoryText: TextView
    private lateinit var userOtsrochkaText: TextView
    private lateinit var text_weight: TextView
    private lateinit var text_illnesses: TextView
    private lateinit var text_blood_group: TextView
    private lateinit var med_id: String

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_military_info, container, false)

        userlast_comText = view.findViewById(R.id.last_com)
        usernext_comText = view.findViewById(R.id.next_com)
        userProffessionText = view.findViewById(R.id.Proffession)
        userDateStartText = view.findViewById(R.id.DateStart)

        text_status_medcomission = view.findViewById(R.id.text_status_medcomission)
        text_date_medcomission = view.findViewById(R.id.text_date_medcomission)
        text_height = view.findViewById(R.id.text_height)
        userCategoryText = view.findViewById(R.id.Category)
        userOtsrochkaText = view.findViewById(R.id.Otsrochka)
        text_weight = view.findViewById(R.id.text_weight)
        text_illnesses = view.findViewById(R.id.text_illnesses)
        text_blood_group = view.findViewById(R.id.text_blood_group)
        med_id = ""



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
                        val Proffession = userData["proffession"] as? String ?: ""
                        val DateStart = userData["datestart"] as? String ?: ""
                        val medcomission_id = userData["medcomission_id"] as? String ?: ""

                        // Устанавливаем значения в TextView
                        userlast_comText.text = last_com
                        usernext_comText.text = next_com
                        userProffessionText.text = Proffession
                        userDateStartText.text = DateStart
                        med_id = medcomission_id

                        // Получаем данные из коллекции "medcomission" после установки med_id
                        val userRefSecond = db.collection("medcomissions").document(med_id)
                        userRefSecond.get()
                            .addOnSuccessListener { documentSnapshotSecond ->
                                if (documentSnapshotSecond.exists()) {
                                    val userDataSecond = documentSnapshotSecond.data
                                    if (userDataSecond != null) {
                                        val status_medcomission = userDataSecond["status"] as? String ?: ""
                                        val date_medcomission = userDataSecond["date"] as? String ?: ""
                                        val Category = userDataSecond["category"] as? String ?: ""
                                        val height = userDataSecond["height"] as? String ?: ""
                                        val Otsrochka = userDataSecond["otsrochka"] as? String ?: ""
                                        val weight = userDataSecond["weight"] as? String ?: ""
                                        val illnesses = userDataSecond["illnesses"] as? String ?: ""
                                        val blood_group = userDataSecond["blood_group"] as? String ?: ""

                                        // Устанавливаем значения в TextView
                                        text_status_medcomission.text = status_medcomission
                                        text_date_medcomission.text = date_medcomission
                                        userCategoryText.text = Category
                                        text_height.text = height
                                        userOtsrochkaText.text = Otsrochka
                                        text_weight.text = weight
                                        text_illnesses.text = illnesses
                                        text_blood_group.text = blood_group
                                    }
                                }
                            }
                    }
                }
            }
            .addOnFailureListener {
                // Обработка ошибки, если не удалось получить данные
            }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = MilitaryInfoFragment()
    }
}