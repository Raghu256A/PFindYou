package com.findyou_professionalapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.findyou_professionalapp.Adapters.BookingOrdersAdapter
import com.findyou_professionalapp.DataClass.BookingData
import com.findyou_professionalapp.R
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.repository.Repository
import com.findyou_professionalapp.viewModelFactory.UserViewModelFactory

class ServiceManagement : AppCompatActivity() {
    private lateinit var btn_AddServices:Button
    private lateinit var vw_back: View
    private lateinit var rc_viewServices: RecyclerView
    private lateinit var progress_bar: ProgressBar
    private lateinit var serviceList: ArrayList<BookingData>
    private val viewModel: UserViewModel
            by viewModels {
                val userRepository = Repository()
                UserViewModelFactory(userRepository)
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_management)
        updateXML()

    }

    private fun updateXML() {
        try {
            vw_back = findViewById(R.id.vw_back)
            btn_AddServices = findViewById(R.id.btn_AddServices)
            rc_viewServices = findViewById(R.id.rc_viewServices)
            progress_bar = findViewById(R.id.progress_bar)
            serviceList = ArrayList()

            rc_viewServices.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
           // ordersAdapter = BookingOrdersAdapter(serviceList, this, viewModel)
           // rc_viewServices.adapter = ordersAdapter
            if (serviceList.isEmpty()) {
                progress_bar.visibility = View.VISIBLE
            } else {
                progress_bar.visibility = View.GONE

            }
            btn_AddServices.setOnClickListener(View.OnClickListener {
                val intent = Intent(this@ServiceManagement,AddNewService::class.java)
                startActivity(intent)
            })

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
                serviceList.clear()
                if (data != null) {
                    serviceList.addAll(data)
                }
                progress_bar.visibility = View.GONE
               // ordersAdapter.updateItems(orderList)

            }
        })
    }
}