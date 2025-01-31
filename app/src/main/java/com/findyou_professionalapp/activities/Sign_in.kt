package com.findyou_professionalapp.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.findyou_professionalapp.R
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.activities.Forgot_Password
import com.findyou_professionalapp.activities.HomeScreen
import com.findyou_professionalapp.activities.Sign_up
import com.findyou_professionalapp.common.Constants
import com.findyou_professionalapp.common.Utils
import com.findyou_professionalapp.repository.Repository
import com.findyou_professionalapp.viewModelFactory.UserViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Sign_in : AppCompatActivity(), View.OnClickListener {
    private lateinit var top_view: View
    private lateinit var et_userId: TextInputEditText
    private lateinit var tv_userId: TextInputLayout
    private lateinit var et_password: TextInputEditText
    private lateinit var tv_password: TextInputLayout
    private lateinit var ll_login_mail: LinearLayout
    private lateinit var ll_login_phone: LinearLayout
    private lateinit var ll_Login: LinearLayout
    private lateinit var tv_signUp: TextView
    private lateinit var tv_forgetPassword: TextView
    private var isPasswordVisible = false
    private var userId: String = "";
    private var password: String = "";
    private val userViewModel: UserViewModel
            by viewModels {
                val userRepository = Repository()
                UserViewModelFactory(userRepository)
            }
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        updateXML()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun updateXML() {
        try {
            top_view = findViewById(R.id.top_view)
            et_userId = findViewById(R.id.et_userId)
            tv_userId = findViewById(R.id.tv_userId)
            et_password = findViewById(R.id.et_password)
            tv_password = findViewById(R.id.tv_password)
            ll_login_mail = findViewById(R.id.ll_login_mail)
            ll_login_phone = findViewById(R.id.ll_login_phone)
            ll_Login = findViewById(R.id.ll_Login)
            tv_forgetPassword = findViewById(R.id.tv_forgetPassword)
            tv_signUp = findViewById(R.id.tv_signUp)
            ll_login_mail.setOnClickListener(this)
            ll_login_phone.setOnClickListener(this)
            ll_Login.setOnClickListener(this)
            tv_forgetPassword.setOnClickListener(this)
            tv_signUp.setOnClickListener(this)
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
            loginStatus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_forgetPassword -> {
                Toast.makeText(this, "Forgot_Password", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Sign_in, Forgot_Password::class.java)
                startActivity(intent)


            }

            R.id.tv_signUp -> {
                Toast.makeText(this, "Sign_up", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Sign_in, Sign_up::class.java)
                startActivity(intent)

            }

            R.id.ll_Login -> {
                val alerts: String? = getAlerts()
                if (alerts.isNullOrEmpty()) {
                    dialog = Utils.openDialog(this)
                    userViewModel.loginWithEmail(userId, password)

                } else {
                    Utils.showAlertDialog(this, "Alert", alerts)
                }
            }


            else -> {
                Toast.makeText(this, "not click", Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun getAlerts(): String? {
        var alert: String? = ""
        userId = et_userId.text.toString().trim()
        password = et_password.text.toString().trim()
        if (userId.isEmpty()) {
            alert += "Please Enter UserId\n"
        }
        if (password.isEmpty()) {
            alert += "Please Enter Password\n"

        }
        if (alert.isNullOrEmpty()) {

            if (Utils.isLoginWithEmail(userId) == true) {
                alert += userId.let { Utils.isValidEmail(it) }
            } else {
                alert += userId.let { Utils.isMobileValid(it, "") }

            }
            alert += password.let { Utils.isPasswordValid(it, "") }
        }

        return alert
    }

    fun loginStatus() {
        userViewModel.loginStatus.observe(this) { status ->
            if (status.first) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeScreen::class.java)
                intent.putExtra("UserId", userId)
                Constants.UserID=userId
                Utils.saveLoginDevice(this@Sign_in,userId)
                startActivity(intent)
                finish()
                Utils.closeDialog(dialog)
            } else {
                Utils.closeDialog(dialog)
                Utils.showAlertDialog(this, "Alert", "Login Failed " + status.second)

            }
        }
    }
}