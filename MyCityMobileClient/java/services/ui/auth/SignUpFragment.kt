package com.example.communityappmobile.services.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.communityappmobile.R
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentSignUpBinding
import com.example.communityappmobile.models.auth.*
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragment: Fragment(), View.OnClickListener {

    private var _binding: FragmentSignUpBinding? = null

    private val binding get() = _binding!!

    private lateinit var nameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    private lateinit var nameLayout: TextInputLayout
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout

    private val emailRegex = Regex("^([\\w-\\.]+)@([\\w-]+\\.)+([\\w-]{2,4})$")

    private val serverApi=ServerAPI.createApi()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        nameEditText=binding.signUpName
        usernameEditText=binding.signUpUsername
        emailEditText=binding.signUpEmail
        passwordEditText=binding.signUpPassword
        confirmPasswordEditText=binding.signUpConfirmPassword

        nameLayout=binding.nameLayout
        usernameLayout=binding.usernameLayout
        emailLayout=binding.emailLayout
        passwordLayout=binding.passwordLayout
        confirmPasswordLayout=binding.confirmPasswordLayout

        nameEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateName()
            }
        }

        usernameEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateUsername()
            }
        }

        emailEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }

        passwordEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }

        confirmPasswordEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
              validateConfirmPassword()
            }
        }


        val signUpButton=binding.signUpButton
        signUpButton.setOnClickListener(this)

        return binding.root
    }

    private fun validateConfirmPassword() {
        if (confirmPasswordEditText.text.toString().length<6) {
            confirmPasswordLayout.error = "Password must be at least 6 characters long"
        } else {
            if(confirmPasswordEditText.text.toString()!=passwordEditText.text.toString()){
                confirmPasswordLayout.error = "Passwords don't match"
            }else{
                passwordLayout.error = null
            }

        }
    }

    private fun validatePassword() {
        if (passwordLayout.editText?.text.toString().length<6) {
            passwordLayout.error = "Password must be at least 6 characters long"
        } else {
            passwordLayout.error = null
        }
    }

    private fun validateEmail() {
        if (emailLayout.editText?.text.toString().isEmpty()) {
            emailLayout.error = "Email is required"
        } else {
            if(!emailEditText.text.matches(emailRegex)){
                emailLayout.error = "Email not valid"
            }else{
                serverApi.emailExists(emailEditText.text.toString()).enqueue(object :
                    Callback<Boolean> {
                    override fun onResponse(
                        call: Call<Boolean>,
                        response: Response<Boolean>
                    ) {
                        Log.e("response",response.toString())
                        if (response.isSuccessful) {
                            val responseBody = response.body() as Boolean
                            if(responseBody){
                                emailLayout.error = "Email already exists"
                            }else{
                                emailLayout.error = null
                            }
                        }
                        else {
                            Log.e("POST_FAIL", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Log.e("POST_FAIL", t.toString())
                    }

                })

            }

        }
    }

    private fun validateUsername() {
        if (usernameLayout.editText?.text.toString().isEmpty()) {
            usernameLayout.error = "Username is required"
        } else {
            Log.e("response",usernameEditText.text.toString().toString())
            serverApi.usernameExists(usernameEditText.text.toString()).enqueue(object :
                Callback<Boolean> {
                override fun onResponse(
                    call: Call<Boolean>,
                    response: Response<Boolean>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body() as Boolean
                        if(responseBody){

                            usernameLayout.error = "Username already exists"
                        }else{
                            usernameLayout.error = null
                        }
                    }
                    else {
                        Log.e("POST_FAIL", response.toString())
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("POST_FAIL", t.toString())
                }

            })

        }
    }

    private fun validateName() {
        if (nameLayout.editText?.text.toString().isEmpty()) {
            nameLayout.error = "Name is required"
        } else {
            nameLayout.error = null
        }
    }

    private fun validate():Boolean{
        validateName()
        validateUsername()
        validateEmail()
        validatePassword()
        validateConfirmPassword()
        if(nameLayout.error==null && usernameLayout.error==null && emailLayout.error==null && passwordLayout.error==null && confirmPasswordLayout.error==null){
            return true
        }
        return false
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(p0: View?) {
        if(p0 is Button){
            if(validate()){
                signUp()
            }

        }
    }


    private fun signUp(){

        val signUpRequest=SignUpRequest(
            nameEditText.text.toString(),
            usernameEditText.text.toString(),
            emailEditText.text.toString(),
            passwordEditText.text.toString()
        )

        val serverAPI = ServerAPI.createApi()
        serverAPI.signUp(signUpRequest).enqueue(object :
            Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                val responseBody = response.body() as MessageResponse
                if (response.isSuccessful) {
                    if(responseBody.message=="Account created successfully!")
                    showAlert(responseBody.message,"Log In","")
                }else{
                    showAlert(responseBody.message,"OK", "")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                showAlert("Error creating account","OK", "Make sure correctly complete all fields")
            }

        })

    }


    private fun showAlert(title: String, positiveButton: String, message: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton) { _, _ ->

            if(positiveButton=="Log In"){
                val authRequest=AuthRequest(usernameEditText.text.toString(),passwordEditText.text.toString())

                val bundle = Bundle().apply {
                    putSerializable("authrequest", authRequest)
                }

                val logInFragment = LogInFragment()
                logInFragment.arguments = bundle
                findNavController().popBackStack(R.id.navigation_log_in,true)
                findNavController().navigate(R.id.navigation_log_in,bundle)
                cleanup()
            }
        }
        builder.show()
    }

    private fun cleanup(){
        super.onDestroyView()
        nameEditText.setText("")
        usernameEditText.setText("")
        emailEditText.setText("")
        passwordEditText.setText("")
        confirmPasswordEditText.setText("")
    }




}
