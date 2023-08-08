package com.example.firebasechattingapplication

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    lateinit var navController: NavController
    private var appContext: Context? = null

    private var editor: SharedPreferences.Editor ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appContext = applicationContext

        val frameLayout = this.findViewById<FrameLayout>(R.id.frameLayout)

// Initialize Firebase Auth
auth = Firebase.auth//        auth = Firebase.auth
        // getting our instance
        // from Firebase Firestore.

        val nestedNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayout) as? NavHostFragment
        navController = nestedNavHostFragment?.navController!!

       if (SpUtils.getString(this , "uid")!=null)
       {
           navController.navigate(R.id.homeFragment)
           getUserProfile()
       }
        else{
           navController.navigate(R.id.loginFragment)
       }


        fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    fun createNewUser(email: String, password: String) {
        val auth = Firebase.auth
       auth.createUserWithEmailAndPassword(email, password)
           .addOnCompleteListener(this) { task ->
               if (task.isSuccessful) {
                   // Sign in success, update UI with the signed-in user's information
                   Log.d(TAG, "createUserWithEmail:success")
                   val user = auth.currentUser
//                   updateUI(user)
               } else {
                   // If sign in fails, display a message to the user.
            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                   Toast.makeText(
                       baseContext,
                       "Authentication failed.",
                       Toast.LENGTH_SHORT,
                   ).show()
//                 updateUI(null)
             }
         }
    }

    fun loginUser(email: String, password: String) {
               val auth = FirebaseAuth.getInstance()
       auth.signInWithEmailAndPassword(email, password)
           .addOnCompleteListener(this) { task ->
               if (task.isSuccessful) {
                   // Sign in success, update UI with the signed-in user's information
                   Log.d(TAG, "signInWithEmail:success")
                   val user = auth.currentUser
                   SpUtils.saveString(this,"uid", auth.currentUser?.uid)
                   navController.navigate(R.id.homeFragment)
//                   updateUI(user)
               } else {
                   // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.exception)
                   Toast.makeText(
                       baseContext,
                       "Authentication failed.",
                       Toast.LENGTH_SHORT,
                   ).show()
//                   updateUI(null)
               }
           }
    }

    fun getUserProfile() {
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid
            Log.d(TAG, "getUserProfile: /// user profile = "+name+email+uid)
        }
    }

    fun updateUserProfile() {
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = "Jane Q. User"
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }

        user!!.updateEmail("user@example.com")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }

        val newPassword = "SOME-SECURE-PASSWORD"

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    fun resetPassword() {
        val emailAddress = "user@example.com"

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    fun reaunthenicateUser() {

        //handle FirebaseAuthRecentLoginRequiredException
        val user = Firebase.auth.currentUser!!

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential("user@example.com", "password1234")

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
    }

    fun deleteUser() {
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
    }


    fun logoutUser() {
        Firebase.auth.signOut()
    }



   override fun onStart() {
      super.onStart()
      // Check if user is signed in (non-null) and update UI accordingly.
      val currentUser = auth?.currentUser
      if (currentUser != null) {
      }
  }




    @RequiresApi(Build.VERSION_CODES.O)
    fun addDataToFirestore(
        message: String,
        senderName: String,
        uid : String
    ) {

        val db = Firebase.firestore

        // creating a collection reference
        // for our Firebase Firestore database.

        //collect name = chat
        val dbCourses = db!!.collection("chat")

        // adding our data to our courses object class.
        val msg = MessageList(message, senderName, uid)

        // below method is use to add data to Firebase Firestore.
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        Log.d(TAG, "addDataToFirestore: ts  ///  " + timeStamp)
        dbCourses.document(timeStamp).set(msg).addOnSuccessListener {
        }
            .addOnFailureListener { e ->
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
            }
    }

    fun getDataFromFirestore() {
        val db = Firebase.firestore

        // creating a collection reference
        // for our Firebase Firestore database.

        val dbCourses = db!!.collection("chat")
        dbCourses
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "data = " + "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "data = Error getting documents: ", exception)
            }

    }

}