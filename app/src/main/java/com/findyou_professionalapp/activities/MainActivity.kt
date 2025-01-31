package com.findyou_professionalapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.findyou_professionalapp.R
import com.findyou_professionalapp.common.Constants
import com.findyou_professionalapp.common.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var view1: View
    private lateinit var view2: View
    private lateinit var view3: View
    private lateinit var view4: View
    private lateinit var view5: View
    private lateinit var ll_main: LinearLayout
    private lateinit var ll_main3: LinearLayout
    private lateinit var Rl_main2: RelativeLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_main)
        view1 = findViewById(R.id.view1)
        view2 = findViewById(R.id.view2)
        view3 = findViewById(R.id.view3)
        view4 = findViewById(R.id.view4)
        view5 = findViewById(R.id.view5)

        ll_main = findViewById(R.id.ll_main)
        Rl_main2 = findViewById(R.id.Rl_main2)
        ll_main3 = findViewById(R.id.ll_main3)
        ll_main.isVisible = true
        Rl_main2.isVisible = false
        ll_main3.isVisible = false
        view5.isVisible = false
        val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)

        val slide_up: Animation = AnimationUtils.loadAnimation(this, R.anim.move_left)
        val slide_down: Animation = AnimationUtils.loadAnimation(this, R.anim.move_right)
        val zoom: Animation = AnimationUtils.loadAnimation(this, R.anim.zoom_animation)
        val downtoUp: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_dowtoup)

        lifecycleScope.launch {
            delay(1000)
            view1.startAnimation(anim)
            view2.startAnimation(anim)
            delay(400)
            ll_main.isVisible = false
            Rl_main2.isVisible = true
            delay(550)
            view3.startAnimation(slide_up)
            view4.startAnimation(slide_down)
            delay(100)
            ll_main.isVisible=false
            Rl_main2.isVisible=false
            ll_main3.isVisible=true
            ll_main3.startAnimation(zoom)
            delay(500)
            ll_main.isVisible=false
            Rl_main2.isVisible=false
            ll_main3.isVisible=false
            view5.isVisible=true
            view5.startAnimation(downtoUp)
            delay(800)
            isLogin()
            /*val intent = Intent(this@MainActivity, Sign_up::class.java)
            startActivity(intent)
            finish()
*/
        }


    }

    private fun isLogin() {
        val sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", "")
        if (token != null && Utils.isTokenValid(token, this)) {
            val id = sharedPreferences.getString(token, "")
            if (id != null) {
                navigateToMenuScreen(id)
            } else {
                navigateToLoginScreen()
            }
        } else {
            navigateToLoginScreen()
        }
    }

    private fun navigateToMenuScreen(id: String) {
        val intent = Intent(this, HomeScreen::class.java)
        Constants.UserID = id
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, Sign_in::class.java)
        startActivity(intent)
        finish()
    }
}