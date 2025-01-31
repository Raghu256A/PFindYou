package com.findyou_professionalapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.findyou_professionalapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class OTPScreen : AppCompatActivity(), View.OnClickListener {
    private lateinit var view_close: View
    private lateinit var et_enter_OTP_bx1: EditText
    private lateinit var et_enter_OTP_bx2: EditText
    private lateinit var et_enter_OTP_bx3: EditText
    private lateinit var et_enter_OTP_bx4: EditText
    private lateinit var et_mailOrPhone: EditText
    private lateinit var btn_verify_otp: Button
    private lateinit var btn_send_otp: Button
    private lateinit var view_close1: View
    private lateinit var ll_verifyOTP: LinearLayout
    private lateinit var ll_SendOTP: LinearLayout
    private lateinit var tv_resend_otp: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = window
        window.setBackgroundDrawableResource(R.drawable.normalbg)
        this.setFinishOnTouchOutside(false)
        setContentView(R.layout.activity_otpscreen)
        updateXML()
    }

    fun updateXML() {
        try {
            view_close = findViewById(R.id.view_close)
            view_close1 = findViewById(R.id.view_close1)
            et_enter_OTP_bx1 = findViewById(R.id.et_enter_OTP_bx1)
            et_enter_OTP_bx2 = findViewById(R.id.et_enter_OTP_bx2)
            et_enter_OTP_bx3 = findViewById(R.id.et_enter_OTP_bx3)
            et_enter_OTP_bx4 = findViewById(R.id.et_enter_OTP_bx4)
            btn_verify_otp = findViewById(R.id.btn_verify_otp)
            et_mailOrPhone = findViewById(R.id.et_mailOrPhone)
            ll_verifyOTP = findViewById(R.id.ll_verifyOTP)
            ll_SendOTP = findViewById(R.id.ll_SendOTP)
            btn_send_otp = findViewById(R.id.btn_send_otp)
            tv_resend_otp = findViewById(R.id.tv_resend_otp)
            view_close.setOnClickListener(this)
            view_close1.setOnClickListener(this)
            btn_verify_otp.setOnClickListener(this)
            btn_send_otp.setOnClickListener(this)
            tv_resend_otp.setOnClickListener(this)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.view_close -> {
               onBackPressed()

            }

            R.id.view_close1 -> {
                onBackPressed()
            }

            else -> {
                Toast.makeText(this, "not click", Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}