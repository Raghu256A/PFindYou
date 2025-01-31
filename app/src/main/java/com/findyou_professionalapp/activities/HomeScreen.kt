package com.findyou_professionalapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.findyou_professionalapp.R
import com.findyou_professionalapp.fragments.ChatFragment
import com.findyou_professionalapp.fragments.DashBoardFragment
import com.findyou_professionalapp.fragments.ProfileFragment
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class HomeScreen : AppCompatActivity() {
    private lateinit var  bottomNavigation: CurvedBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        //Constants.UserID= intent.getStringExtra("UserId").toString()?:""
        bottomNavigation = findViewById(R.id.bottomNavigation)

        bottomNavigation.add(
            CurvedBottomNavigation.Model(1,"Home",R.drawable.dashboard_ic)
        )

        bottomNavigation.add(
            CurvedBottomNavigation.Model(2,"Chat",R.drawable.chat_ic)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3,"Profile",R.drawable.profile_ic)
        )


        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> {
                    replaceFragment(DashBoardFragment())
                }
                2 -> {
                    replaceFragment(ChatFragment())
                }
                3->{
                    replaceFragment(ProfileFragment())
                }
            }
        }

        replaceFragment(DashBoardFragment())
        bottomNavigation.show(1)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,fragment)
            .commit()
    }
}