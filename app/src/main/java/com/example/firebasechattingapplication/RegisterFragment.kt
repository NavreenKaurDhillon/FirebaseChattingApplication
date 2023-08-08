package com.example.firebasechattingapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasechattingapplication.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment(), View.OnClickListener {
    lateinit var registerBinding: FragmentRegisterBinding
    val mainActivity = MainActivity()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return registerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerBT = view.findViewById<AppCompatButton>(R.id.registerrBT)
        val loginTV = view.findViewById<TextView>(R.id.loginTV)

        registerBT.setOnClickListener(this)
        loginTV.setOnClickListener (this)

        }
    fun validateData() {
        if (registerBinding.nameET.text.toString().isNullOrEmpty() == true) {
            registerBinding.nameET.setError("Name can't be empty")
        }
       else if (registerBinding.emailET.text.toString().isNullOrEmpty() == true) {
            registerBinding.emailET.setError("Email can't be empty")
        }
        else if (registerBinding.passwordET.text.toString().isNullOrEmpty() == true) {
            registerBinding.passwordET.setError("Password can't be empty")
        }
        else if (registerBinding.cpasswordET.text.toString().isNullOrEmpty() == true) {
            registerBinding.cpasswordET.setError("Confirm password can't be empty")
        }
        else{

            SpUtils.saveString(requireContext() ,"uid", FirebaseAuth.getInstance().currentUser?.uid.toString())
            registerUser()
        }
    }

    fun registerUser() {
        SpUtils.saveString(requireContext() , "name" , registerBinding.nameET.text.toString())
     mainActivity.createNewUser(registerBinding.emailET.text.toString().trim(), registerBinding.passwordET.text.toString().trim())
//        if (registered == 1) {
//            Toast.makeText(requireContext(), "Registered successfully", Toast.LENGTH_LONG).show()
//            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
//        } else {
//
//        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.registerrBT -> {
                    validateData()

                }
                R.id.loginTV ->
                {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }

    }
}