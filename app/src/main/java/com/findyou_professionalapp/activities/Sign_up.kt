package com.findyou_professionalapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.eoshopping.runtime_permission.RunTimePermissions
import com.findyou_professionalapp.DataClass.ProfessionalsData
import com.findyou_professionalapp.R
import com.findyou_professionalapp.activities.Sign_in
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.common.Constants
import com.findyou_professionalapp.common.Utils
import com.findyou_professionalapp.repository.Repository
import com.findyou_professionalapp.viewModelFactory.UserViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

class Sign_up : AppCompatActivity(), View.OnClickListener {
    private lateinit var top_view: View
    private lateinit var et_userName: TextInputEditText
    private lateinit var et_profession: TextInputEditText
    private lateinit var tv_userName: TextInputLayout
    private lateinit var et_password: TextInputEditText
    private lateinit var tv_password: TextInputLayout
    private lateinit var et_Phone: TextInputEditText
    private lateinit var tv_Phone: TextInputLayout
    private lateinit var tv_email: TextInputLayout
    private lateinit var et_email: TextInputEditText
    private lateinit var et_DOB: TextInputEditText
    private lateinit var et_confirm_password: TextInputEditText
    private lateinit var tv_confirm_password: TextInputLayout
    private lateinit var ll_singup_mail: LinearLayout
    private lateinit var ll_singup_fb: LinearLayout
    private lateinit var ll_signUp: LinearLayout
    private lateinit var tv_login: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var RB_F: RadioButton
    private lateinit var RB_M: RadioButton
    private var isPasswordVisible = false
    private var isCPasswordVisible = false
    private var isMale = false
    private var isFemale = false
    private var fullName: String? = ""
    private var phoneNumber: String? = ""
    private var email: String? = ""
    private var password: String? = ""
    private var confirm_password: String? = ""
    private var dob: String? = ""
    private var profession: String? = ""
    private val userViewModel: UserViewModel by viewModels {
        val userRepository = Repository()
        UserViewModelFactory(userRepository)
    }
    private lateinit var dialog: ProgressDialog
    private val REQUEST_CODE_CAMERA = 100
    private val REQUEST_CODE_GALLERY = 101
    private lateinit var imgProfile: ShapeableImageView
    private lateinit var bnt_Update_pic: ImageButton
    private var profilePicUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signUpStatus()
        updateXML()

        RunTimePermissions.checkAllPermissions(this)


    }

    @SuppressLint("ClickableViewAccessibility")
    fun updateXML() {
        try {
            top_view = findViewById(R.id.top_view)
            et_userName = findViewById(R.id.et_userName)
            tv_userName = findViewById(R.id.tv_userName)
            et_password = findViewById(R.id.et_password)
            tv_password = findViewById(R.id.tv_password)
            tv_Phone = findViewById(R.id.tv_Phone)
            et_Phone = findViewById(R.id.et_Phone)
            tv_email = findViewById(R.id.tv_email)
            et_email = findViewById(R.id.et_email)
            et_profession = findViewById(R.id.et_profession)
            imgProfile = findViewById<ShapeableImageView>(R.id.img_profile)
            bnt_Update_pic = findViewById(R.id.bnt_Update_pic)

            tv_confirm_password = findViewById(R.id.tv_confirm_password)
            et_confirm_password = findViewById(R.id.et_confirm_password)
            ll_singup_mail = findViewById(R.id.ll_singup_mail)
            ll_singup_fb = findViewById(R.id.ll_singup_fb)
            ll_signUp = findViewById(R.id.ll_signUp)
            tv_login = findViewById(R.id.tv_login)
            et_DOB = findViewById(R.id.et_DOB)
            radioGroup = findViewById(R.id.radioGroup)
            RB_F = findViewById(R.id.RB_F)
            RB_M = findViewById(R.id.RB_M)
            ll_singup_mail.setOnClickListener(this)
            ll_singup_fb.setOnClickListener(this)
            ll_signUp.setOnClickListener(this)
            bnt_Update_pic.setOnClickListener(this)
            tv_login.setOnClickListener(this)
            et_password.setOnTouchListener { _, event ->
                if (event.action == android.view.MotionEvent.ACTION_UP) {
                    val drawableEnd = 2
                    if (event.rawX >= et_password.right - et_password.compoundDrawables[drawableEnd].bounds.width()) {
                        isPasswordVisible =
                            Utils.togglePasswordVisibility(isPasswordVisible, et_password)
                        return@setOnTouchListener true
                    }
                }
                false
            }
            et_confirm_password.setOnTouchListener { _, event ->
                if (event.action == android.view.MotionEvent.ACTION_UP) {
                    val drawableEnd = 2
                    if (event.rawX >= et_confirm_password.right - et_confirm_password.compoundDrawables[drawableEnd].bounds.width()) {
                        isCPasswordVisible =
                            Utils.togglePasswordVisibility(isCPasswordVisible, et_confirm_password)
                        return@setOnTouchListener true
                    }
                }
                false
            }
            et_DOB.setOnTouchListener { _, event ->
                if (event.action == android.view.MotionEvent.ACTION_UP) {
                    val drawableEnd = 2
                    if (event.rawX >= et_DOB.right - et_DOB.compoundDrawables[drawableEnd].bounds.width()) {
                        dob = et_DOB.text.toString().trim()
                        getDOB(dob!!)


                        return@setOnTouchListener true
                    }
                }
                false
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_login -> {
                Toast.makeText(this, "login", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Sign_up, Sign_in::class.java)
                startActivity(intent)
                finish()

            }

            R.id.ll_signUp -> {
                var alert: String? = getAlerts()
                if (alert.isNullOrEmpty()) {
                    dialog = Utils.openDialog(this)
                    saveData()

                } else {
                    Utils.showAlertDialog(this, "Alert", alert)
                }

            }

            R.id.bnt_Update_pic -> {
                selectImageSource()

            }


            else -> {
                Toast.makeText(this, "not function there", Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun getAlerts(): String? {
        var alert: String? = ""
        fullName = et_userName.text.toString()
        phoneNumber = et_Phone.text.toString()
        email = et_email.text.toString()
        password = et_password.text.toString()
        isMale = RB_M.isChecked
        isFemale = RB_F.isChecked
        profession = et_profession.text.toString()
        confirm_password = et_confirm_password.text.toString()
        dob = et_DOB.text.toString()
        if (fullName.isNullOrEmpty()) {
            alert += "Please enter your full name\n"
        }
        if (phoneNumber.isNullOrEmpty()) {
            alert += "Please enter phone\n"
        }
        if (password.isNullOrEmpty()) {
            alert += "Please enter password\n"
        }
        if (confirm_password.isNullOrEmpty()) {
            alert += "Please enter confirm password\n"

        }
        if (!isFemale && !isMale) {
            alert += "user gender is mandatory\n"

        }
        if (dob.isNullOrEmpty()) {
            alert += "user DOB is mandatory\n"

        }
        if (profession.isNullOrEmpty()) {
            alert += "user Profession is mandatory\n"

        }
        if (profilePicUri == null) {
           // alert += "profile image is mandatory\n"

        }
        if (alert.isNullOrEmpty()) {
            if (!confirm_password.isNullOrEmpty() && !confirm_password.equals(password)) {
                alert += "Confirm password and password should not be match\n"

            }
            alert += phoneNumber?.let { Utils.isMobileValid(it, "") }
            alert += email?.let { Utils.isValidEmail(it) }
            alert += password?.let { Utils.isPasswordValid(it, "") }
            alert += confirm_password?.let { Utils.isPasswordValid(it, "Confirm ") }
        }


        return alert
    }

    fun getDOB(str_date: String) {
        var dob: String? = ""
        try {
            val builder = AlertDialog.Builder(this)
            val inflater: LayoutInflater = LayoutInflater.from(this)
            val view: View = inflater.inflate(R.layout.datepicker, null, false)
            val date = view.findViewById<DatePicker>(R.id.date_picker)
            builder.setView(view)
            if (!str_date.isEmpty()) {
                val selectedDate: Date = Utils.stringToDateTimeDefault(
                    str_date.replace("/", " ").replace("/", " "),
                    "dd MM yyyy"
                )
                val CDate = Calendar.getInstance()
                CDate.time = selectedDate
                date.init(
                    CDate[Calendar.YEAR],
                    CDate[Calendar.MONTH], CDate[Calendar.DAY_OF_MONTH], null
                )
            } else {
                val defaultDate: Date = Utils.stringToDateTimeDefault(Utils.getDate(), "dd MM yyyy")
                val CDate = Calendar.getInstance()
                CDate.time = defaultDate
                date.init(
                    CDate[Calendar.YEAR],
                    CDate[Calendar.MONTH], CDate[Calendar.DAY_OF_MONTH], null
                )
            }
            builder.setPositiveButton(
                "Set"
            ) { dialog, which ->

                dob =
                    Utils.getDayNumber(date.dayOfMonth) + "/" + Utils.getMonthNumber(date.month) + "/" + date.year
                et_DOB.setText(dob)
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> return@OnClickListener })
            builder.setCancelable(false)
            builder.setTitle("Select Date")
            val dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun signUpStatus() {

        try {

            userViewModel.loginStatus.observe(this) { status ->
                if (status.first) {
                    Utils.closeDialog(dialog)
                    Toast.makeText(this, "Register Successful", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeScreen::class.java)
                    intent.putExtra("UserId", email)
                    Constants.UserID= email.toString()
                    Utils.saveLoginDevice(this@Sign_up,email.toString())
                    startActivity(intent)
                    finish()

                } else {
                    Utils.closeDialog(dialog)
                    Utils.showAlertDialog(this, "Alert", "Register Failed")

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun saveData() {
        try {
            var gender = ""
            if (isMale) {
                gender = "M"
            } else {
                gender = "F"
            }
            val rating = Random.nextInt(6)

            val user = ProfessionalsData(
                name = fullName!!, phoneNumber = phoneNumber!!,
                emailID = email!!, password = (password!!), gender = gender, userDOB = dob,
                profession = profession, rating = rating, profileImageUrl = null
            )
            userViewModel.registerUser(user, profilePicUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun selectImageSource() {
        val options = arrayOf("Camera", "Gallery")
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Select Image Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri? {
        val file = File(cacheDir, "profile_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return Uri.fromFile(file)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    profilePicUri = saveBitmapToFile(imageBitmap)
                }

                REQUEST_CODE_GALLERY -> {
                    profilePicUri = data?.data
                }
            }
            if (profilePicUri != null) {
                imgProfile.setImageURI(profilePicUri)

            }
        }

    }

}