package com.example.communityappmobile.services.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.communityappmobile.R
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentLogInBinding
import com.example.communityappmobile.models.auth.AuthRequest
import com.example.communityappmobile.models.auth.AuthResponse
import com.example.communityappmobile.models.auth.SharedPreferencesHelper
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.services.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInFragment: Fragment(), View.OnClickListener {

    private var _binding: FragmentLogInBinding? = null

    private val binding get() = _binding!!


    private var usernameEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private lateinit var authMessage: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLogInBinding.inflate(inflater, container, false)

        usernameEditText=binding.username
        passwordEditText=binding.password
        authMessage=binding.authMessage

        val authRequest = (arguments?.getSerializable("authrequest") as? AuthRequest)
        if(authRequest!=null){
            usernameEditText?.setText(authRequest.username)
            passwordEditText?.setText(authRequest.password)
        }

        val logInButton=binding.logIn
        logInButton.setOnClickListener(this)
        val signUpButton=binding.goToSignUpButton
        signUpButton.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(p0: View?) {
        if(p0 is Button){
            if(p0.id==R.id.log_in){
                val serverAPI = ServerAPI.createApi()
                serverAPI.authenticate(AuthRequest(usernameEditText?.text.toString(), passwordEditText?.text.toString())).enqueue(object :
                    Callback<AuthResponse> {
                    override fun onResponse(
                        call: Call<AuthResponse>,
                        response: Response<AuthResponse>
                    ) {
                        Log.e("r",response.toString())
                        if (response.isSuccessful) {
                            authMessage.visibility=View.GONE
                            val responseBody = response.body() as AuthResponse
                            if (responseBody.role.equals("Regular User")) {
                                User.token = responseBody.token
                                User.id = responseBody.id
                                User.name = responseBody.name
                                User.username = responseBody.username
                                User.email=responseBody.email
                                SharedPreferencesHelper.setUser(requireActivity())
                                SharedPreferencesHelper.setLoggedIn(requireActivity(), true)
                                val intent = Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(intent)
                            } else {
                                authMessage.visibility=View.VISIBLE
                            }

                        }else{
                            authMessage.visibility=View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        authMessage.visibility=View.VISIBLE
                        Log.e("AUTH_FAIL", t.toString())
                    }

                })
            }else{
                findNavController().navigate(R.id.navigation_sign_up)
            }
        }
    }


}