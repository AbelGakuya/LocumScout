package com.locums.locumscout.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.locums.locumscout.data.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseViewModel:  ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _profileData = MutableLiveData<Profile>()

    val profileData: LiveData<Profile> = _profileData

    fun fetchProfileData(userId: String){
        GlobalScope.launch(Dispatchers.IO) {
            val userRef = db.collection("doctor_users").document(userId)
            userRef.get().addOnSuccessListener {
                documentSnapshot ->
                if (documentSnapshot.exists()){
                    val imageUrl = documentSnapshot.getString("imageUrl")
                    val username = documentSnapshot.getString("name")
                    val profileData = Profile(username,imageUrl)

                    _profileData.postValue(profileData)
                }
            }

        }
    }




}