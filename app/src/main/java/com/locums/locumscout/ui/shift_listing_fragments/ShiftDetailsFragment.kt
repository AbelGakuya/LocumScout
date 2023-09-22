package com.locums.locumscout.ui.shift_listing_fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.locums.locumscout.R
//import com.locums.locumscout.data.ApiClient
import com.locums.locumscout.data.NotificationData
import com.locums.locumscout.data.PushNotification
import com.locums.locumscout.databinding.FragmentShiftDetailsBinding
import com.locums.locumscout.other.Constants.coverLetterDownloadUrl
import com.locums.locumscout.other.Constants.endDate
import com.locums.locumscout.other.Constants.hospitalId
import com.locums.locumscout.other.Constants.name
import com.locums.locumscout.other.Constants.resumeDownloadUrl
import com.locums.locumscout.other.Constants.startDate
import com.locums.locumscout.other.Constants.vacancyId
import com.locums.locumscout.retrofit.RetrofitInstance
import com.locums.locumscout.viewModels.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class ShiftDetailsFragment : Fragment() {
    private var _binding: FragmentShiftDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar
    private lateinit var dialog: AlertDialog
    private lateinit var btnSubmitApplication: Button
    lateinit var auth: FirebaseAuth
    val sharedViewModel2: SharedViewModel by activityViewModels()

    val TAG = "ShiftDetails"


  //  private val args: ShiftDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShiftDetailsBinding.inflate(inflater,container,false)
        val view = binding.root


        sharedViewModel2.content.observe(viewLifecycleOwner, {
                content ->
            hospitalId = content.uid
            vacancyId = content.vacancyId
            startDate = content.startDate.toString()
            endDate = content.endDate.toString()
            binding.hospitalName.text = content.hospitalName
            binding.jobTitle.text = content.title
            binding.btnApply.text = "Apply to ${content.hospitalName}"
            Glide.with(binding.hospitalImage.context)
                .load(content.imageUrl)
                .into(binding.hospitalImage)
        })


      auth = FirebaseAuth.getInstance()

      return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnApply.setOnClickListener {
            openAdditionalInfoDialog()
        }



    }

    // Define the function to retrieve hospital's FCM token
    private fun getHospitalFcmTokenN(hospitalId: String?, callback: (String?) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        CoroutineScope(Dispatchers.IO).launch {

            val hospitalRef = firestore.collection("hospital_users").document(hospitalId!!)
            hospitalRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val hospitalData = task.result?.data
                    val fcmToken = hospitalData?.get("fcmToken") as? String

                    //Toast.makeText(requireContext(),"token Successfully!", Toast.LENGTH_LONG).show()
                    callback(fcmToken) // Pass the token to the callback function
                } else {
                    callback(null) // Pass null if there's an error
                }
            }
        }

    }




    private fun openAdditionalInfoDialog(){

        val dialogView = layoutInflater.inflate(R.layout.additional_info_form,null)
        val  resumeEditText = dialogView.findViewById<EditText>(R.id.editTextResume)
        val coverLetterEditText = dialogView.findViewById<EditText>(R.id.editTextCoverLetter)
        progressBar = dialogView.findViewById(R.id.progressBar)
        btnSubmitApplication = dialogView.findViewById<Button>(R.id.submit_application)

        dialog = AlertDialog.Builder(requireContext())
            .setTitle("Provide additional information")
            .setView(dialogView)
            .create()
        resumeEditText.setOnClickListener {
            openFilePickerResume.launch("application/pdf")

        }
        coverLetterEditText.setOnClickListener {
            openFilePickerCoverLetter.launch("application/pdf")
        }


        btnSubmitApplication.setOnClickListener {


            getHospitalFcmToken(hospitalId!!)
//            CoroutineScope(Dispatchers.IO).launch {
//             //   getHospitalFcmToken()
//               // submitApplication()
//                getHospitalFcmToken(hospitalId)
//            }

            dialog.dismiss()

           // Toast.makeText(requireContext(),"Applied Successfully!", Toast.LENGTH_LONG).show()

        }



        dialog.show()


    }

    private val openFilePickerResume = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
        uri?.let {
        CoroutineScope(Dispatchers.Main).launch {
            uploadResumeToFirebase(uri)

        }

        }
    }

    private val openFilePickerCoverLetter = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        uri?.let {
            CoroutineScope(Dispatchers.Main).launch {
                uploadCoverLetterToFirebase(uri)

            }

        }
    }

    private fun updateProgress(progress: Int){
        progressBar.progress = progress
    }

    private suspend fun uploadResumeToFirebase(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance()
        val fileName = uri.lastPathSegment ?: "file"
        val fileRef = storageRef.reference.child("files/$fileName")
        try {
                val uploadTask = fileRef.putFile(uri)
                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    //update UI with progress

                    updateProgress(progress)


                }.await()
                val downloadUrl = fileRef.downloadUrl.await().toString()
                //save url to database
            resumeDownloadUrl = downloadUrl

            if ((coverLetterDownloadUrl == "")){
                btnSubmitApplication.isEnabled = false

            } else{
                btnSubmitApplication.isEnabled = true
            }

                //store user data in Firestore using coroutines
//                val userMap = mapOf( "resumeUrl" to downloadUrl)
//                withContext(Dispatchers.IO) {
//                    val userRef = auth.currentUser?.uid?.let {
//                        FirebaseFirestore.getInstance()
//                            .collection("application")
//                            .document(it)
//                    }
//                    userRef?.set(userMap)?.await() ?: ""
//                }
            Toast.makeText(requireContext(),"Uploaded Successfully!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                //handle exception
            }
    }

    private suspend fun uploadCoverLetterToFirebase(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance()
        val fileName = uri.lastPathSegment ?: "file"
        val fileRef = storageRef.reference.child("files/$fileName")
        try {
            val uploadTask = fileRef.putFile(uri)
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress =
                    (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                //update UI with progress
                updateProgress(progress)
            }.await()
            val downloadUrl = fileRef.downloadUrl.await().toString()
            //save url to database
            coverLetterDownloadUrl = downloadUrl
            if ((resumeDownloadUrl == "") ){
                btnSubmitApplication.isEnabled = false
            } else{
                btnSubmitApplication.isEnabled = true
            }

            //store user data in Firestore using coroutines
//            val userMap = mapOf( "coverLetterUrl" to downloadUrl)
//            withContext(Dispatchers.IO) {
//                val userRef = auth.currentUser?.uid?.let {
//                    FirebaseFirestore.getInstance()
//                        .collection("application")
//                        .document(it)
//                }
//                userRef?.set(userMap)?.await() ?: ""
//            }
            Toast.makeText(requireContext(),"Uploaded Successfully!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            //handle exception
        }
    }

    private fun submitApplication() {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = auth.currentUser?.uid
            val userMap = mapOf( "Applicants_name" to name,
                "Applicant's_uid" to uid,
                "Applicants coverLetter" to coverLetterDownloadUrl,
                "Applicant's resume" to resumeDownloadUrl,
                "vacancyId" to vacancyId,
                "application_status" to false,
                "hospitalId" to hospitalId)

                    val userRef = auth.currentUser?.uid?.let {
                        FirebaseFirestore.getInstance()
                            .collection("applications")
                            .document(it)
                    }
                    userRef?.set(userMap)?.await() ?: ""

            withContext(Dispatchers.Main){
                Toast.makeText(requireContext(), "Application made successfully", Toast.LENGTH_LONG).show()
            }


            //getHospitalFcmToken(hospitalId,uid)


          // triggerCloudFunction()
                    //sendNotification2()

        }
    }

    private fun getHospitalFcmToken(hospitalId: String?) {
        val firestore = FirebaseFirestore.getInstance()
        CoroutineScope(Dispatchers.IO).launch {
            val hospitalRef = firestore.collection("hospitals").document(hospitalId!!)
            hospitalRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val hospitalData = task.result?.data
                    val fcmToken = hospitalData?.get("fcmToken") as? String

                    if (!fcmToken.isNullOrEmpty()){
                        val title =  "New Application"
                        val message = "You have a new application from $name"
                        val applicantId = auth.currentUser?.uid

                        PushNotification(
                            NotificationData(title, message,applicantId,hospitalId,
                                vacancyId,startDate,endDate),
                            fcmToken
                        ).also {
                            sendNotification(it)
                        }

                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "No token", Toast.LENGTH_LONG).show()
                        }
                    }


                    //Toast.makeText(requireContext(),"token Successfully!", Toast.LENGTH_LONG).show()
                    // Pass the token to the callback function
                } else {
                    // Pass null if there's an error
                }
            }.addOnFailureListener{
                exception ->
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), " firestore query failur $exception", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun sendNotification1(fcmToken: String?,applicationId:String?) {
        val message = RemoteMessage.Builder(fcmToken!!)
            .setMessageId(applicationId!!)
            .addData("title", "New Application")
            .addData("body", "You have a new application from $name")
            .build()

        try {
            FirebaseMessaging.getInstance().send(message)
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), " notification sent succesfully", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception){
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), " notification sending failure $e", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun sendNotification(notification: PushNotification)
    = CoroutineScope(Dispatchers.IO).launch{
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                submitApplication()
                if (response.isSuccessful){
                    suspend { Log.d(TAG, "Response: ${Gson().toJson(response)}")  }

                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e: Exception){
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), " notification not sent succesfully catch block  ${e}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun triggerCloudFunction(){

            val httpClient = OkHttpClient()
            val requestBody = FormBody.Builder()
                .add("hospitalId", hospitalId!!)
                .add("doctorName", "Abel")
                .build()
            val request = Request.Builder()
                .url("https://us-central1-locumscout.cloudfunctions.net/sendNotification")
                .post(requestBody)
                .build()
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    activity?.runOnUiThread {
                        if (response.isSuccessful) {


                            Toast.makeText(
                                requireContext(),
                                "Response succesful",
                                Toast.LENGTH_LONG
                            )
                                .show()


                        } else {
                            val errorBody = response.body?.string()
                            Toast.makeText(requireContext(),"Response not succesful $errorBody", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })


    }

//
//    fun sendNotification(){
//        getHospitalFcmToken(hospitalId) { fcmToken ->
//            if (fcmToken != null) {
//                //Toast.makeText(requireContext(),"token Successfully!", Toast.LENGTH_LONG).show()
//                val notificationData = """
//                    {
//                        "title": "New Application",
//                        "body": "A healthcare worker has applied for a vacancy.",
//                        "tokens": ["$fcmToken"]
//                    }
//                """.trimIndent()
//                //  val serverUrl = "http://localhost:3000" // Replace with your server URL
//                val serverUrl =
//                    "http://192.168.1.193:3000" // Replace with your actual local IP address and port
//
//                val url = URL("$serverUrl/send-notification")
//                val connection = url.openConnection() as HttpURLConnection
//                Toast.makeText(requireContext(),"token Successfully!", Toast.LENGTH_LONG).show()
//                try {
//                    connection.requestMethod = "POST"
//                    connection.setRequestProperty("Content-Type", "application/json")
//                    connection.setRequestProperty("Accept", "application/json")
//                    connection.doOutput = true
//
//                    val os: OutputStream = connection.outputStream
//                    os.write(notificationData.toByteArray(Charsets.UTF_8))
//                    os.close()
//
//                    val responseCode = connection.responseCode
//                    Toast.makeText(requireContext(), "try Successfully!", Toast.LENGTH_LONG)
//                        .show()
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        // Notification sent successfully
//                        val response =
//                            connection.inputStream.bufferedReader().use { it.readText() }
//                        // Toast.makeText(requireContext(),"Notification sent successfully: $response", Toast.LENGTH_LONG).show()
//                        // println("Notification sent successfully: $response")
//                        Toast.makeText(
//                            requireContext(),
//                            "Notification Successfully!",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    } else {
//                        // Handle error
//                        Toast.makeText(
//                            requireContext(),
//                            "Notification not sent ",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        // println("Error sending notification: HTTP $responseCode")
//                        Toast.makeText(
//                            requireContext(),
//                            "token not Successfully!",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(
//                        requireContext(),
//                        "Notification not Successfully! $e",
//                        Toast.LENGTH_LONG
//                    ).show()
//                } finally {
//                    connection.disconnect()
//                }
//
//            }
//        }
//
//    }
//
//    fun sendNotification2(){
//        getHospitalFcmToken(hospitalId) { fcmToken ->
//            if (fcmToken != null) {
//                val title = "Notification Title"
//                val body = "Notification Body"
//
//                val tokens = listOf(fcmToken)
//
//                val request = NotificationRequest(title, body, tokens)
//
//                // Use a coroutine to perform the network request in a background thread
//                viewLifecycleOwner.lifecycleScope.launch {
//                    try {
//                        val response = withContext(Dispatchers.IO) {
//                            ApiClient.apiService.sendNotification(request)
//                        }
//                        handleResponse(response)
//                    } catch (e: Exception) {
//                        handleError(e)
//                    }
//                }
//            } else {
//                // Handle the case when the FCM token is null
//            }
//        }
//    }
//
//
//
//    private fun handleResponse(response: NotificationResponse) {
//        // Handle the successful response, e.g., update UI
//
//        Toast.makeText(
//            requireContext(),
//            "Notification Successfully! $response",
//            Toast.LENGTH_LONG
//        ).show()
//    }
//
//    private fun handleError(exception: Exception) {
//        // Handle the error, e.g., show an error message
//        Toast.makeText(
//            requireContext(),
//            "Notification Successfully! $exception",
//            Toast.LENGTH_LONG
//        ).show()
//    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

}





//        uploadTask.continueWithTask {
//            task ->
//            if (!task.isSuccessful){
//                task.exception?.let {
//                    throw it
//                }
//            }
//
//
//        }.addOnCompleteListener {
//            downloadUrlTask ->
//            if (downloadUrlTask.isSuccessful){
//                val downloadUrl = downloadUrlTask.result
//                //save url to database
//            } else {
//                //handle failure
//            }
//        }






