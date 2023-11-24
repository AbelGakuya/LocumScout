package com.locums.locumscout.ui.authenticationfragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.locums.locumscout.R
import com.locums.locumscout.data.Doctor
import com.locums.locumscout.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID


class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    lateinit var auth: FirebaseAuth
    private lateinit var filePath: Uri
    private val PICK_IMAGE_REQUEST = 22

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentRegistrationBinding.inflate(inflater,container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors")


        binding.btnRegister.setOnClickListener {
            registerUser()
            //findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }
        binding.profileImage.setOnClickListener {
            selectImage()
        }

        return view
    }

    private fun registerUser(){
        val email = binding.edtEmail.text.trim().toString()
        val password = binding.edtPassword.text.trim().toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                   // updateProfile1()
                    uploadImageToFirebase()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }

                } catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun checkLoggedInState(){
        if (auth.currentUser == null){
            Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT).show()
        } else {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }
    }

    private fun updateProfile(){
        auth.currentUser?.let {user->
            val firstName = binding.firstName.text.trim().toString()
            val lastName = binding.lastName.text.trim().toString()
            val username = firstName + " " + lastName

            val photoUri = filePath
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(photoUri)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    user.updateProfile(profileUpdates).await()

                    // Save additional data to Firestore
                    val userId = user.uid
                    val userData = hashMapOf(
                        "username" to username,
                        "profilePictureUrl" to photoUri.toString()
                    )
                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection("users").document(userId).set(userData).await()

                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Successfully updated profile", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //getting user's details to add them to firebase database
    private fun updateProfile1(){
        auth.currentUser?.let {user->
            val uid = auth.currentUser!!.uid
            val firstName = binding.firstName.text.trim().toString()
            val lastName = binding.lastName.text.trim().toString()
            val username = firstName + " " + lastName
            val doctor = Doctor(firstName, lastName,username,uid)
            addToDatabase(doctor)

        }
    }

    private fun addToDatabase(doctor: Doctor) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uid = auth.currentUser?.uid
                if (uid != null){
                    databaseReference.child(uid).setValue(doctor).await()
                    uploadImageToFirebase()
                   // uploadImage()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                }
            } catch (e:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImage() {
        val  uid = auth.currentUser?.uid
        val filename = UUID.randomUUID().toString()
        storageReference = FirebaseStorage.getInstance().getReference("/images/$filename")
        storageReference.putFile(filePath).addOnSuccessListener {
//            hideProgressBar()

                Toast.makeText(context, "Profile Successfully Updated", Toast.LENGTH_SHORT).show()

                storageReference.downloadUrl.addOnSuccessListener {
                    it.toString()
                    if (uid != null) {
                        databaseReference.child(uid).child("imageUrl").setValue(it.toString())
                    }
                }

        //  val downloadUri: Task<Uri> = storageReference.downloadUrl
                /*    if (uid != null) {
                        databaseReference.child(uid).child("imageUrl").setValue("/images/$filename")
                    }*/
            }.addOnFailureListener{
//            hideProgressBar()
                Toast.makeText(context,"Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }


    private suspend fun uploadImageToFirebase(){
        val firstName = binding.firstName.text.trim().toString()
        val lastName = binding.lastName.text.trim().toString()
        val username = firstName + " " + lastName
        withContext(Dispatchers.IO){
            val storageRef = FirebaseStorage.getInstance().reference.child("user_images/${auth.currentUser?.uid}.jpg")
            storageRef.putFile(filePath).await()
            val imageUrl = storageRef.downloadUrl.await().toString()

            //store user data in Firestore using coroutines
            val userMap = mapOf("name" to username, "imageUrl" to imageUrl)
            val userRef = auth.currentUser?.uid?.let {
                FirebaseFirestore.getInstance()
                    .collection("doctor_users")
                    .document(it)
            }
            userRef?.set(userMap)?.await() ?: ""


        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data!!
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        getActivity()?.getContentResolver(),
                        filePath
                    )
                binding.profileImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }


}