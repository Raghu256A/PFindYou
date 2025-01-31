package com.findyou_professionalapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.findyou_professionalapp.R
import com.findyou_professionalapp.activities.Sign_in

class Forgot_Password : AppCompatActivity(), View.OnClickListener {
    private lateinit var vw_back: View
    private lateinit var et_mailOrPhone: EditText
    private lateinit var btn_send_otp: Button
    private lateinit var tv_backToLogin: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        updateXML()
    }

    fun updateXML() {
        try {
            vw_back = findViewById(R.id.vw_back)
            et_mailOrPhone = findViewById(R.id.et_mailOrPhone)
            btn_send_otp = findViewById(R.id.btn_send_otp)
            tv_backToLogin = findViewById(R.id.tv_backToLogin)
            vw_back.setOnClickListener(this)
            btn_send_otp.setOnClickListener(this)
            tv_backToLogin.setOnClickListener(this)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_backToLogin -> {
                Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Forgot_Password, Sign_in::class.java)
                startActivity(intent)
                finish()

            }

            R.id.vw_back -> {
                Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Forgot_Password, Sign_in::class.java)
                startActivity(intent)

            }

            R.id.btn_send_otp -> {
                Toast.makeText(this, "Send OTP", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Forgot_Password, OTPScreen::class.java)
                startActivity(intent)

            }

            else -> {
                Toast.makeText(this, "not click", Toast.LENGTH_SHORT).show()

            }
        }
    }
}