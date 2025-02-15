package com.findyou_professionalapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.findyou_professionalapp.Adapters.BookingOrdersAdapter
import com.findyou_professionalapp.DataClass.BookingData
import com.findyou_professionalapp.R
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.repository.Repository
import com.findyou_professionalapp.viewModelFactory.UserViewModelFactory
import com.google.android.material.navigation.NavigationView

class BookingManagement : AppCompatActivity() {
    private lateinit var vw_back: View
    private lateinit var et_search_bar: EditText
    private lateinit var vw_filter: View
    private lateinit var tv_searchCount: TextView
    private lateinit var rc_showBookings: RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var progress_bar: ProgressBar
    private lateinit var ordersAdapter: BookingOrdersAdapter
    private lateinit var orderList: ArrayList<BookingData>

    private val viewModel: UserViewModel
            by viewModels {
                val userRepository = Repository()
                UserViewModelFactory(userRepository)
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking_management)
        updateXML()

    }

    private fun updateXML() {
        try {
            vw_back = findViewById(R.id.vw_back)
            rc_showBookings = findViewById(R.id.rc_showBookings)
            et_search_bar = findViewById(R.id.et_search_bar)
            vw_filter = findViewById(R.id.vw_filter)
            tv_searchCount = findViewById(R.id.tv_searchCount)
            drawerLayout = findViewById(R.id.main)
            navView = findViewById(R.id.navView)
            progress_bar = findViewById(R.id.progress_bar)
            orderList = ArrayList()

            rc_showBookings.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            ordersAdapter = BookingOrdersAdapter(orderList, this, viewModel)
            rc_showBookings.adapter = ordersAdapter
            if (orderList.isEmpty()) {
                progress_bar.visibility = View.VISIBLE
            } else {
                progress_bar.visibility = View.GONE

            }

            getBookings()
        } catch (e: Exception) {
            Log.e("BookingManagement", e.message.toString())
        }
    }

    private fun getBookings() {
        progress_bar.visibility = View.VISIBLE

        viewModel.getAllBookingOrders()
        viewModel.bookingOrders.observe(this, Observer { data ->
            run {
                orderList.clear()
                if (data != null) {
                    orderList.addAll(data)
                }
                progress_bar.visibility = View.GONE
                ordersAdapter.updateItems(orderList)

            }
        })
    }

}