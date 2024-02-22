package com.example.communityappmobile.services.ui.events

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
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

import com.example.communityappmobile.databinding.FragmentCreateEventBinding

import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.events.Event

import com.example.communityappmobile.models.file.UploadResponse

import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class CreateEventFragment: Fragment(), View.OnClickListener {

    private var _binding: FragmentCreateEventBinding? = null

    private val binding get() = _binding!!

    private lateinit var titleLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var typeLayout: TextInputLayout
    private lateinit var locationLayout: TextInputLayout
    private lateinit var photoLayout: TextInputLayout
    private lateinit var startDateLayout: TextInputLayout
    private lateinit var startTimeLayout: TextInputLayout
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var typeEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var photoEditText: EditText
    private lateinit var startDateInput:EditText
    private lateinit var endDateInput:EditText
    private lateinit var startTimeInput:EditText
    private lateinit var endTimeInput:EditText


    private var startDate: LocalDate?=null
    private var endDate: LocalDate?=null
    private var startTime: LocalTime?=null
    private var endTime:LocalTime?=null



    private lateinit var createEventViewModel: CreateEventViewModel

    private lateinit var photoUrl: String

    private lateinit var requestedPermission: String

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var photoFile: File
    private lateinit var photoUri: Uri


    val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createEventViewModel = ViewModelProvider(this).get(CreateEventViewModel::class.java)

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)

        titleEditText=binding.eventTitleInput
        descriptionEditText=binding.eventDescriptionInput
        typeEditText=binding.eventTypesField
        locationEditText=binding.eventLocationInput

        createEventViewModel.getTitle().observe(viewLifecycleOwner
        ) { title -> titleEditText.setText(title) }

        createEventViewModel.getDescription().observe(viewLifecycleOwner
        ) { description -> descriptionEditText.setText(description) }

        createEventViewModel.getType().observe(viewLifecycleOwner
        ) { type -> typeEditText.setText(type) }

        createEventViewModel.getLocation().observe(viewLifecycleOwner
        ) { location -> locationEditText.setText(location) }

        createEventViewModel.getPhoto().observe(viewLifecycleOwner
        ) { photo -> if(photo!=null)photoEditText.setText("Uploaded") }


        photoEditText=binding.uploadImageInput
        photoEditText.setOnClickListener { view->showPopupMenu(view) }

        startDateInput=binding.eventStartDateInput
        startDateInput.setOnClickListener(View.OnClickListener {
            showDatePickerDialog("Start date")
        })

        endDateInput=binding.eventEndDateInput
        endDateInput.setOnClickListener(View.OnClickListener {
            showDatePickerDialog("End date")
        })

        startTimeInput=binding.eventStartTimeInput
        startTimeInput.setOnClickListener(View.OnClickListener {
            showTimePickerDialog("Start time")
        })

        endTimeInput=binding.eventEndTimeInput
        endTimeInput.setOnClickListener(View.OnClickListener {
            showTimePickerDialog("End time")
        })

        titleLayout=binding.eventTitleLayout
        descriptionLayout=binding.eventDescriptionLayout
        typeLayout=binding.eventTypeLayout
        locationLayout=binding.eventLocationLayout
        photoLayout=binding.eventPhotoLayout
        startDateLayout=binding.eventStartDateLayout
        startTimeLayout=binding.eventStartTimeLayout


        titleEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateField(titleLayout,"Title")
            }
        }

        descriptionEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateField(descriptionLayout,"Description")
            }
        }

        typeEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateField(typeLayout,"Type")
            }
        }

        locationEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateField(locationLayout,"Location")
            }
        }

        val eventTypes = listOf(
            "Concert",
            "Party",
            "Festival",
            "Show",
            "Sports",
            "Exhibition",
            "Book Launch",
            "Social",
            "Educational",
            "Business",
            "Movie",
            "Diverse"
        )
        val adapter = ArrayAdapter(requireContext(), R.layout.menu_list_item, eventTypes)
        (typeEditText as AutoCompleteTextView).setAdapter(adapter)

        val submitButton=binding.createEventButton
        submitButton.setOnClickListener(this)

        return binding.root
    }

    private fun showDatePickerDialog(title: String) {
        val materialDatePicker=MaterialDatePicker.Builder.datePicker().setTitleText(title).build()
        materialDatePicker.addOnPositiveButtonClickListener {
            if(title=="Start date"){
                startDate=LocalDate.parse(materialDatePicker.headerText,dateFormatter)
                if(endDate!=null && (endDate?.isBefore(startDate) == true) ){
                    createAlertBuilder("End date cannot be before start date")
                    startDate=null
                    startDateInput.setText("")
                }else{
                    startDateInput.setText(materialDatePicker.headerText)
                }

            }else{
                if(startDate==null){
                   createAlertBuilder("Please select a start date first")
                }else{
                    endDate=LocalDate.parse(materialDatePicker.headerText,dateFormatter)
                    if(endDate?.isBefore(startDate) == true){
                        createAlertBuilder("End date cannot be before start date")
                        endDate=null
                    }else{
                        endDateInput.setText(materialDatePicker.headerText)
                    }
                }
            }
        }
        materialDatePicker.show(requireActivity().supportFragmentManager,"DATE")
    }

    private fun showTimePickerDialog(title: String) {
        val materialTimePicker=MaterialTimePicker.Builder().setTitleText(title).setTimeFormat(
            TimeFormat.CLOCK_24H).setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD).build()
        materialTimePicker.addOnPositiveButtonClickListener {
            if(title=="Start time"){
                startTime=LocalTime.of(materialTimePicker.hour,materialTimePicker.minute)
                if(startDateInput.text.toString()==endDateInput.text.toString() && endTime!=null && (startTime?.isAfter(endTime) == true)){
                    createAlertBuilder("End time cannot be before start time")
                    startTime=null
                }else{
                    startTimeInput.setText("%02d".format(materialTimePicker.hour)+":"+"%02d".format(materialTimePicker.minute))
                }

            }else{
                endTime=LocalTime.of(materialTimePicker.hour,materialTimePicker.minute)
                if(startDateInput.text.toString()==endDateInput.text.toString() && (startTime?.isAfter(endTime) == true)){
                    createAlertBuilder("End time cannot be before start time")
                    endTime=null
                }else{
                    endTimeInput.setText("%02d".format(materialTimePicker.hour)+":"+"%02d".format(materialTimePicker.minute))

                }
            }
        }
        materialTimePicker.show(requireActivity().supportFragmentManager,"TIME")
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
            createEventViewModel.setPhoto(photoFile)
            photoEditText.setText("Uploaded")
        }
    }

    private val pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri=data?.data!!
            createEventViewModel.setPhoto(File(getRealPathFromURI(uri)!!))
            photoEditText.setText("Uploaded")
        }
    }

    private fun takePhoto() {
        photoFile=createPhotoFile()!!
        photoUri= FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName + ".provider", photoFile);
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri )
        takePhotoLauncher.launch(takePhotoIntent)
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhotoLauncher.launch(pickPhotoIntent)
    }




    override fun onClick(p0: View?) {
        if(p0 is Button){
            if(validate()){
                uploadPhoto()
            }
        }
    }

    private fun uploadPhoto(){
        val file = createEventViewModel.getPhoto().value!!
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val fileUploadAPI= FileUploadAPI.createApi()
        fileUploadAPI.uploadFile(filePart).enqueue(object:
            Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {

                if(response.isSuccessful){
                    val responseBody=response.body() as UploadResponse
                    photoUrl=responseBody.publicUrl
                    submitEvent()
                }else{
                    Log.e("error",response.toString())
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


    private fun submitEvent(){
        val endDateTime: String = if (endDate==null || endTime==null){
            ""
        }else{
            endDate.toString()+" "+endTime.toString()
        }
        val event= Event(
            0,
            User.username,
            typeEditText.text.toString(),
            titleEditText.text.toString(),
            photoUrl,
            startDate.toString()+" "+startTime.toString(),
            endDateTime,
            descriptionEditText.text.toString(),
            locationEditText.text.toString(),
            "Created",
            0
        )

        Log.e("event",event.toString())

        val serverAPI= ServerAPI.createApi()
        serverAPI.createEvent(event).enqueue(object:
            Callback<Event> {
            override fun onResponse(
                call: Call<Event>,
                response: Response<Event>
            ) {

                if(response.isSuccessful){
                    showSuccessAlert()
                }
                else{
                    Log.e("CREATE_FAIL",response.toString())
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
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

    private fun showSuccessAlert(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Event created successfully")
        builder.setMessage("Waiting for event approval from the administrator. When approved, your event will be visible to other users. You can check its status in \"My Events\" page")
        builder.setPositiveButton("OK") { _, _ ->
            cleanup()
            findNavController().popBackStack(R.id.navigation_my_events,true)
            findNavController().navigate(R.id.navigation_my_events)

        }
        builder.show()
    }

    private fun cleanup(){
        super.onDestroyView()
        createEventViewModel.setType("")
        createEventViewModel.setLocation("")
        createEventViewModel.setTitle("")
        createEventViewModel.setDescription("")
        createEventViewModel.setPhoto(null)
        titleEditText.setText("")
        descriptionEditText.setText("")
        typeEditText.setText("")
        locationEditText.setText("")
        photoEditText.setText("")
    }

    private fun createAlertBuilder(title: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setPositiveButton("OK") { _, _ ->

        }
        builder.show()
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
        validateField(startDateLayout,"Start Date")
        validateField(startTimeLayout,"Start Time")

        if(titleLayout.error!=null || typeLayout.error!=null || locationLayout.error!=null || descriptionLayout.error!=null || photoLayout.error!=null  || startDateLayout.error!=null  || startTimeLayout.error!=null){
            return false
        }
        return true
    }



}
