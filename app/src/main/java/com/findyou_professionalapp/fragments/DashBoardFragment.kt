package com.findyou_professionalapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.findyou_professionalapp.R
import com.findyou_professionalapp.activities.ServiceManagement
import com.google.android.material.navigation.NavigationView

class DashBoardFragment : Fragment() {
    private lateinit var btn_serviceManagement:Button
    private lateinit var btn_viewWorks:Button
    private lateinit var btn_viewEarns:Button
    private lateinit var tv_viewBookings:TextView
    private lateinit var tv_Name:TextView
    private lateinit var tv_wish:TextView
    private lateinit var tv_location:TextView
    private lateinit var progress_text:TextView
    private lateinit var tv_earns:TextView
    private lateinit var et_search_bar:EditText
    private lateinit var rc_showBookings:RecyclerView
    private lateinit var navView:NavigationView
    private lateinit var main:DrawerLayout
    private lateinit var vw_notification:View
    private lateinit var vw_currency:View
    private lateinit var vw_currency1:View
    private lateinit var progress_bar:ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View=inflater.inflate(R.layout.fragment_dash_board, container, false)
       updateXML(view)
        return view
    }

    private fun updateXML(view: View){
        try {
            btn_serviceManagement=view.findViewById(R.id.btn_serviceManagement)
            btn_viewWorks=view.findViewById(R.id.btn_viewWorks)
            btn_viewEarns=view.findViewById(R.id.btn_viewEarns)
            tv_viewBookings=view.findViewById(R.id.tv_viewBookings)
            tv_Name=view.findViewById(R.id.tv_Name)
            tv_wish=view.findViewById(R.id.tv_wish)
            tv_location=view.findViewById(R.id.tv_location)
            progress_text=view.findViewById(R.id.progress_text)
            tv_earns=view.findViewById(R.id.tv_earns)
            et_search_bar=view.findViewById(R.id.et_search_bar)
            rc_showBookings=view.findViewById(R.id.rc_showBookings)
            navView=view.findViewById(R.id.navView)
            main=view.findViewById(R.id.main)
            vw_notification=view.findViewById(R.id.vw_notification)
            vw_currency=view.findViewById(R.id.vw_currency)
            vw_currency1=view.findViewById(R.id.vw_currency1)
            progress_bar=view.findViewById(R.id.progress_bar)

            btn_serviceManagement.setOnClickListener(View.OnClickListener {
                val intent=Intent(requireContext(),ServiceManagement::class.java)
                startActivity(intent)
            })
            btn_viewEarns.setOnClickListener(View.OnClickListener {

            })
            btn_viewWorks.setOnClickListener(View.OnClickListener {

            })
            tv_viewBookings.setOnClickListener(View.OnClickListener {

            })


        }catch (e:Exception){
            Log.e("dashBoard",e.message.toString())
        }
    }
}