package com.example.communityappmobile.services.ui.profile


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.communityappmobile.R
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentProfileBinding
import com.example.communityappmobile.models.auth.*
import com.example.communityappmobile.models.user.UserAnalytics
import com.example.communityappmobile.models.user.UserUpdate
import com.example.communityappmobile.services.MainActivity
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(){

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private lateinit var nameEditText: EditText
    private lateinit var nameLayout: TextInputLayout

    private lateinit var passwordEditText: EditText
    private lateinit var passwordLayout: TextInputLayout

    private lateinit var confirmPasswordEditText: EditText
    private lateinit var confirmPasswordLayout: TextInputLayout

    private val serverAPI = ServerAPI.createApi()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        serverAPI.getUserAnalytics(User.id).enqueue(object :
            Callback<UserAnalytics> {
            override fun onResponse(
                call: Call<UserAnalytics>,
                response: Response<UserAnalytics>
            ) {

                if (response.isSuccessful) {
                    val analytics=response.body() as UserAnalytics
                    binding.numberOfIssues.text=analytics.issues.toString()
                    binding.numberOfEvents.text=analytics.events.toString()

                }
                else {
                    Log.e("GET_FAIL", response.toString())
                }
            }

            override fun onFailure(call: Call<UserAnalytics>, t: Throwable) {
                Log.e("GET_FAIL", t.toString())
            }

        })


        binding.userName.text= User.name
        binding.userUsername.text=User.username
        binding.userEmail.text=User.email

        val changeNameButton=binding.goToChangeName
        val changePasswordButton=binding.goToChangePassword
        val deleteAccountButton=binding.initiateDeleteAccount
        val logOutButton=binding.logOut

        changeNameButton.setOnClickListener {

            val nameDialogView: View = layoutInflater.inflate(R.layout.dialog_change_name, null)
            nameEditText=nameDialogView.findViewById<EditText>(R.id.change_name)
            nameLayout=nameDialogView.findViewById<TextInputLayout>(R.id.change_name_layout)
            nameEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    validateName()
                }
            }

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Change Name")
            nameEditText.setText(User.name)
            builder.setView(nameDialogView)
            builder.setPositiveButton("Save") { _, _ ->

                validateName()
                if(nameLayout.error==null){
                    updateUser(true)
                }else{
                    Toast.makeText(context, "Name change failed", Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton("Cancel") { _, _ ->
                Log.d("action","cancelled")
            }
            builder.show()
        }

        changePasswordButton.setOnClickListener {
            val passwordDialogView: View = layoutInflater.inflate(R.layout.dialog_change_password, null)

            passwordEditText=passwordDialogView.findViewById<EditText>(R.id.change_password)
            confirmPasswordEditText=passwordDialogView.findViewById<EditText>(R.id.change_password_confirm)
            passwordLayout=passwordDialogView.findViewById<TextInputLayout>(R.id.change_password_layout)
            confirmPasswordLayout=passwordDialogView.findViewById<TextInputLayout>(R.id.confirm_change_password_layout)

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

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Change Password")
            builder.setView(passwordDialogView)

            builder.setPositiveButton("Save") { _, _ ->
                validatePassword()
                validateConfirmPassword()
                if(passwordLayout.error==null && confirmPasswordLayout.error==null){
                    updateUser(false)
                }else{
                    Toast.makeText(context, "Password change failed", Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton("Cancel") { _, _ ->
                Log.d("action","cancelled")
            }
            builder.show()
        }

        deleteAccountButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Are you sure you want to delete your account?")
            builder.setMessage("This action cannot be undone.")
            builder.setPositiveButton("Yes") { _, _ ->
               deleteUser()
            }

            builder.setNegativeButton("No") { _, _ ->
            }
            builder.show()
        }

        logOutButton.setOnClickListener {
            User.token = ""
            User.id = 0
            User.name = ""
            User.username = ""
            User.email=""
            SharedPreferencesHelper.setUser(requireActivity())
            SharedPreferencesHelper.setLoggedIn(requireActivity(), false)
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun updateUser(isNameUpdate: Boolean) {
        val updatedUser= UserUpdate(User.id,"",User.username,User.email,"")
        if(isNameUpdate){
            updatedUser.name=nameEditText.text.toString()
        }else{
            updatedUser.name=User.name
            updatedUser.password=passwordEditText.text.toString()
        }
        serverAPI.updateUser(updatedUser).enqueue(object :
            Callback<UserUpdate> {
            override fun onResponse(
                call: Call<UserUpdate>,
                response: Response<UserUpdate>
            ) {

                if (response.isSuccessful) {
                    val responseBody = response.body() as UserUpdate
                    if(isNameUpdate){
                        User.name=responseBody.name
                        SharedPreferencesHelper.setUser(requireContext())
                        binding.userName.text = responseBody.name
                        Toast.makeText(requireContext(),"Name updated successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),"Password updated successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Log.e("UPDATE_FAIL", response.toString())
                }
            }

            override fun onFailure(call: Call<UserUpdate>, t: Throwable) {
                Log.e("UPDATE_FAIL", t.toString())
            }

        })
    }

    private fun deleteUser(){
        serverAPI.deleteUser(User.id).enqueue(object :
            Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {

                if (response.isSuccessful) {

                    User.token = ""
                    User.id = 0
                    User.name = ""
                    User.username = ""
                    User.email=""
                    SharedPreferencesHelper.setUser(requireActivity())
                    SharedPreferencesHelper.setLoggedIn(requireActivity(), false)
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)

                }
                else {
                    Log.e("DELETE_FAIL", response.toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("DELETE_FAIL", t.toString())
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateName(){
        if(nameEditText.text.toString().isEmpty()){
            nameLayout.error="Name cannot be empty"
        }else{
            nameLayout.error=null
        }
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



}