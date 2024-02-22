package com.example.communityappmobile.services.ui.issues

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.communityappmobile.R
import com.example.communityappmobile.apis.FileUploadAPI
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentReportIssueBinding
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.file.UploadResponse
import com.example.communityappmobile.models.issue.Issue
import com.example.communityappmobile.models.issue.NewIssue
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ReportIssueFragment: Fragment(), OnClickListener {

    private var _binding: FragmentReportIssueBinding? = null

    private val binding get() = _binding!!

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var typeEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var photoEditText: EditText
    private lateinit var reportIssueViewModel: ReportIssueViewModel

    private lateinit var titleLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var typeLayout: TextInputLayout
    private lateinit var locationLayout: TextInputLayout
    private lateinit var photoLayout: TextInputLayout

    private lateinit var photoUrl: String

    private lateinit var requestedPermission: String

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var photoFile: File
    private lateinit var photoUri: Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reportIssueViewModel = ViewModelProvider(requireActivity()).get(ReportIssueViewModel::class.java)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (Manifest.permission.READ_EXTERNAL_STORAGE == requestedPermission) {
                openGallery()
            } else if (Manifest.permission.CAMERA == requestedPermission) {
                takePhoto()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        typeLayout=binding.issueTypeLayout
        val eventTypeAdapter = ArrayAdapter(requireContext(), R.layout.menu_list_item, reportIssueViewModel.getIssueTypes())
        (typeLayout.editText as AutoCompleteTextView).setAdapter(eventTypeAdapter)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReportIssueBinding.inflate(inflater, container, false)

        val args: Bundle? =arguments
        if ((args != null) && args.containsKey("coordinates")) {
            val coordinates = args.getParcelable<LatLng>("coordinates")
            if (coordinates != null) {
                val coordinatesString: String=coordinates.latitude.toString()+","+coordinates.longitude.toString()
                reportIssueViewModel.setCoordinates(coordinatesString)
                locationEditText=binding.chooseLocationInput
                val gcd = Geocoder(context, Locale.getDefault())
                val addresses: List<Address> = gcd.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
                if (addresses.isNotEmpty()) {
                    locationEditText.setText(addresses[0].getAddressLine(0))
                } else {
                    locationEditText.setText("Selected")
                }
            }
        }


        titleEditText=binding.issueTitleInput
        descriptionEditText=binding.issueDescriptionInput
        typeEditText=binding.issueTypesField
        locationEditText=binding.chooseLocationInput

        titleLayout=binding.issueTitleLayout
        descriptionLayout=binding.issueDescriptionLayout
        locationLayout=binding.issueLocationLayout
        photoLayout=binding.issuePhotoLayout
        typeLayout=binding.issueTypeLayout

        reportIssueViewModel.getTitle().observe(viewLifecycleOwner
        ) { title -> titleEditText.setText(title) }

        reportIssueViewModel.getDescription().observe(viewLifecycleOwner
        ) { description -> descriptionEditText.setText(description) }

        reportIssueViewModel.getType().observe(viewLifecycleOwner
        ) { type -> typeEditText.setText(type) }

        reportIssueViewModel.getPhoto().observe(viewLifecycleOwner
        ) { photo -> if(photo!=null)photoEditText.setText("Uploaded") }

        photoEditText=binding.uploadImageInput
        photoEditText.setOnClickListener { view->showPopupMenu(view) }

        val chooseLocationInput=binding.chooseLocationInput
        chooseLocationInput.setOnClickListener { view->chooseLocation(view) }

        val adapter = ArrayAdapter(requireContext(), R.layout.menu_list_item, reportIssueViewModel!!.getIssueTypes())
        (typeEditText as AutoCompleteTextView).setAdapter(adapter)


        titleEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateField(titleLayout, "Title")
            }
        }

        descriptionEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateField(descriptionLayout, "Description")
            }
        }

        typeEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateField(typeLayout, "Type")
            }
        }


        val newForm = (arguments?.getSerializable("new_form") as?  Boolean)
        if(newForm == true){
            reportIssueViewModel.setType("")
            reportIssueViewModel.setCoordinates("")
            reportIssueViewModel.setTitle("")
            reportIssueViewModel.setDescription("")
            reportIssueViewModel.setPhoto(null)
            titleEditText.setText("")
            descriptionEditText.setText("")
            typeEditText.setText("")
            locationEditText.setText("")
            photoEditText.setText("")
        }

        val submitButton=binding.submitIssue
        submitButton.setOnClickListener(this)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.upload_photo_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_take_photo -> {
                    requestCameraPermission()
                    true
                }
                R.id.menu_item_upload_photo -> {
                    requestStoragePermission()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            reportIssueViewModel.setPhoto(photoFile)
            photoEditText.setText("Uploaded")
        }
    }

    private val pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri=data?.data!!
            reportIssueViewModel.setPhoto(File(getRealPathFromURI(uri)!!))
            photoEditText.setText("Uploaded")
        }
    }

    private fun takePhoto() {
        photoFile=createPhotoFile()!!
        photoUri=FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName + ".provider", photoFile);
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri )
        takePhotoLauncher.launch(takePhotoIntent)
    }

    private fun openGallery() {
        photoFile=createPhotoFile()!!
        photoUri=FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName + ".provider", photoFile);
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri )
        pickPhotoLauncher.launch(pickPhotoIntent)
    }


    private fun chooseLocation(view: View?) {
        findNavController().navigate(R.id.navigation_issue_location)
    }

    override fun onPause() {
        super.onPause()
        reportIssueViewModel.setTitle(titleEditText.text.toString())
        reportIssueViewModel.setDescription(descriptionEditText.text.toString())
        reportIssueViewModel.setType(typeEditText.text.toString())

    }

    override fun onClick(p0: View?) {
        if(p0 is Button){
            if(validate()){
                uploadPhoto()
            }
        }
    }

    private fun uploadPhoto(){
        val file = reportIssueViewModel.getPhoto().value!!
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val fileUploadAPI=FileUploadAPI.createApi()
        fileUploadAPI.uploadFile(filePart).enqueue(object:
            Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {

                if(response.isSuccessful){
                    val responseBody=response.body() as UploadResponse
                    photoUrl=responseBody.publicUrl
                    submitIssue()
                }else{
                    Log.e("UPLOAD_FAIL",response.toString())
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.e("UPLOAD_FAIL",t.toString())
            }

        })
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context?.contentResolver?.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(columnIndex)
            }
        }
        return filePath
    }


    private fun submitIssue(){
        val issueTitle=reportIssueViewModel.getTitle().value.toString()
        val issueType=reportIssueViewModel.getType().value.toString()
        val issueDescription=reportIssueViewModel.getDescription().value.toString()
        val issueCoordinates=reportIssueViewModel.getCoordinates().value.toString()
        val username=User.username
        val issueLocation=locationEditText.text.toString()
        val issueImage=photoUrl
        val issue=NewIssue(username,issueType,issueTitle,issueDescription,issueLocation,issueCoordinates,issueImage )

        val serverAPI=ServerAPI.createApi()
        serverAPI.createIssue(issue).enqueue(object:
                    Callback<Issue> {
            override fun onResponse(
                call: Call<Issue>,
                response: Response<Issue>
            ) {

                if(response.isSuccessful){
                    val savedIssue=response.body() as Issue
                    showSuccessAlert(savedIssue)
                }
                else{
                    Log.e("CREATE_FAIL",response.toString())
                }
            }

            override fun onFailure(call: Call<Issue>, t: Throwable) {
                Log.e("CREATE_FAIL",t.toString())
            }
        }
        )
    }

    private fun requestStoragePermission() {
        requestedPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        requestPermissionLauncher.launch(requestedPermission)
    }

    private fun requestCameraPermission() {
        requestedPermission = Manifest.permission.CAMERA
        requestPermissionLauncher.launch(requestedPermission)
    }

    private fun createPhotoFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun showSuccessAlert(issue: Issue){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Issue submitted successfully")
        builder.setMessage("You can view the submitted issue details or return to the Issues page")
        builder.setPositiveButton("View") { _, _ ->
            findNavController().popBackStack(R.id.navigation_issues,false)
            cleanup()

            val bundle = Bundle().apply {
                putSerializable("issue", issue)
            }

            val issueDetailsFragment = IssueDetailsFragment()
            issueDetailsFragment.arguments = bundle
            findNavController().navigate(R.id.navigation_issue_details,bundle)
        }

        builder.setNegativeButton("Return") { _, _ ->
            findNavController().popBackStack(R.id.navigation_issues,false)
        }
        builder.show()
    }

    private fun cleanup(){
        super.onDestroyView()
        reportIssueViewModel.setType("")
        reportIssueViewModel.setCoordinates("")
        reportIssueViewModel.setTitle("")
        reportIssueViewModel.setDescription("")
        reportIssueViewModel.setPhoto(null)
        titleEditText.setText("")
        descriptionEditText.setText("")
        typeEditText.setText("")
        locationEditText.setText("")
        photoEditText.setText("")

    }

    private fun validateField(layout: TextInputLayout,field:String) {
        if (layout.editText?.text.toString().isEmpty()) {
            layout.error = "$field is required"
        } else {
            layout.error = null
        }
    }

    private fun validate():Boolean{
        validateField(titleLayout,"Title")
        validateField(typeLayout,"Type")
        validateField(locationLayout,"Location")
        validateField(descriptionLayout,"Description")
        validateField(photoLayout,"Photo")

        if(titleLayout.error!=null || typeLayout.error!=null || locationLayout.error!=null || descriptionLayout.error!=null || photoLayout.error!=null ){
            return false
        }
        return true
    }


}
