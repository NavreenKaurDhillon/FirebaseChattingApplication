package com.example.firebasechattingapplication


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.firebasechattingapplication.databinding.FragmentChatScreenBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var chatScreenBinding: FragmentChatScreenBinding
    var mainActivity = MainActivity()
    val messageList = ArrayList<MessageList>()
    var messagesAdapter = MessagesAdapter()
    val CHANNEL_ID : String = "ID"
    val CHANNEL_NAME = "notification channel"
    val CHANNEL_DESCRIPTION = "NOTIFICATION"

    // the String form of link for
    // opening the GFG home-page
    val link1 = "https://www.geeksforgeeks.org/"

    // the String form of link for opening
    // the GFG contribution-page
    val link2 = "https://www.geeksforgeeks.org/contribute/"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_screen , container , false)
        return chatScreenBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageList.clear()
        Log.d(TAG, "onViewCreated: uuid fetched = "+SpUtils.getString(requireContext() , "uid"))
        Log.d(TAG, "onViewCreated: name fetched = "+SpUtils.getString(requireContext() , "name"))

//        getDataFromFirestore()
        // Reference to users collection in Firestore
        val collectionReference = Firebase.firestore.collection("chat")

        // Add snapshot change listener to our collection reference
        collectionReference.addSnapshotListener { snapshot, exception ->
            Log.d(TAG, "onViewCreated: /// snapshot "+ (snapshot?.documentChanges ))

            getDataFromFirestore()
//            for (document in snapshot?.documents!!) {
//                Log.d(ContentValues.TAG, "data = "+ "${document.id} => ${document.data?.get("senderName")}  msg = ${document.data?.get("message")}")
//                if (document.data?.get("senderName").toString()!="" ||
//                    document.data?.get("senderName").toString().isNullOrEmpty()==false  ){
//                    messageList.add(MessageList( document.data?.get("message").toString(), document.data?.get("senderName")
//                        .toString() ))
//                }
//            }
//            chatScreenBinding.messagesRV.adapter=messagesAdapter
//            messagesAdapter.submitList(messageList)
//            messagesAdapter.notifyDataSetChanged()
        }
        chatScreenBinding.sendBT.setOnClickListener {
            if (chatScreenBinding.messageET.text.toString().isNullOrEmpty()==false)
            {
                Log.d(TAG, "onViewCreated: /// id = "+SpUtils.getString(requireContext(), "uid"))
                mainActivity.addDataToFirestore(chatScreenBinding.messageET.text.toString() ,
                    SpUtils.getString(requireContext(),"name").toString(),
                    SpUtils.getString(requireContext() , "uid").toString()
                )
//                getDataFromFirestore()
            }
        }

    }
    fun getDataFromFirestore()
    {
        messageList.clear()
        val  db = Firebase.firestore

        // creating a collection reference
        // for our Firebase Firestore database.
        fun WeightsLifted() {}
        val dbCourses = db!!.collection("chat")
       dbCourses
            .get()
            .addOnSuccessListener { result ->

//                if (document != null) {
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                } else {
//                    Log.d(TAG, "No such document")
//                }
                for (document in result) {
                    Log.d(TAG, "data = "+ "${document.id} => ${document.data.get("senderName")}  msg = ${document.data.get("message")}")

                    if (document.data.get("senderName").toString()!="" ||
                        document.data.get("senderName").toString().isNullOrEmpty()==false  ){
                        messageList.add(MessageList( document.data.get("message").toString(), document.data.get("senderName").toString(), document.data.get("senderUid").toString() ))
                    }
                }
                Log.d(TAG, "getDataFromFirestore: /// size = "+messageList.size)
                chatScreenBinding.messagesRV.adapter=messagesAdapter
                messagesAdapter.submitList(messageList)
                messagesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "data = Error getting documents: ", exception)
            }
    }


    // The function gfgOpenerIntent() returns
    // an Implicit Intent to open a webpage
    private fun gfgOpenerIntent(link: String): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(link)
        return intent
    }
    }