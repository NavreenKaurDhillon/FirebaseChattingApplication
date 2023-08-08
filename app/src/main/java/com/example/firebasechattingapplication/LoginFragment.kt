package com.example.firebasechattingapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.example.firebasechattingapplication.databinding.FragmentLoginBinding
import kotlin.math.log

class LoginFragment : Fragment(), View.OnClickListener {
    lateinit var loginBinding: FragmentLoginBinding
    var mainActivity = MainActivity()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginBinding = DataBindingUtil.inflate(inflater , R.layout.fragment_login , container, false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBinding.loginBT.setOnClickListener (this)
        loginBinding.registerTV.setOnClickListener (this)
    }
    fun validateData()
    {
        if (loginBinding.emailET.text.toString().isNullOrEmpty() == true )
        {
            loginBinding.emailET.setError("Email can't be empty")
        }
       else if (loginBinding.passwordET.text.toString().isNullOrEmpty()==true){
            loginBinding.passwordET.setError("Password can't be empty")
        }
        else{
            validateCredentials()
        }
    }
    fun validateCredentials()
    {
        if (loginBinding.emailET.text.toString()!=null || loginBinding.passwordET.text.toString()!= null)
        {
           mainActivity.loginUser(loginBinding.emailET.text.toString() , loginBinding.passwordET.text.toString())
//            if (logged == 1){
//                Toast.makeText(requireContext() , "Logged in successfully", Toast.LENGTH_LONG).show()
//                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//
//            }
//            else{
//                Toast.makeText(requireContext() , "Invalid credentials", Toast.LENGTH_LONG).show()
//            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.loginBT -> {
                validateData()
            }
            R.id.registerTV -> {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

}