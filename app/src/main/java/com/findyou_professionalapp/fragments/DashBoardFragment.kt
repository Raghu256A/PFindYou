package com.findyou_professionalapp.fragments

import android.content.Intent
import android.os.Bundle
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.findyou_professionalapp.Adapters.BookingOrdersAdapter
import com.findyou_professionalapp.DataClass.BookingData
import com.findyou_professionalapp.R
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.activities.BookingManagement
import com.findyou_professionalapp.activities.ServiceManagement
import com.findyou_professionalapp.common.Constants
import com.findyou_professionalapp.common.LocationHelper
import com.findyou_professionalapp.common.Utils
import com.findyou_professionalapp.repository.Repository
import com.findyou_professionalapp.viewModelFactory.UserViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONObject
import java.io.IOException
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
    private lateinit var ordersAdapter: BookingOrdersAdapter
    private lateinit var orderList: ArrayList<BookingData>

    private val viewModel: UserViewModel
            by viewModels {
                val userRepository = Repository()
                UserViewModelFactory(userRepository)
            }



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
            orderList= arrayListOf()

            btn_serviceManagement.setOnClickListener(View.OnClickListener {
                val intent=Intent(requireContext(),ServiceManagement::class.java)
                startActivity(intent)
            })
            btn_viewEarns.setOnClickListener(View.OnClickListener {

            })
            btn_viewWorks.setOnClickListener(View.OnClickListener {

            })
            tv_viewBookings.setOnClickListener(View.OnClickListener {
                val intent=Intent(requireContext(),BookingManagement::class.java)
                startActivity(intent)
            })


        }catch (e:Exception){
            Log.e("dashBoard",e.message.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocationHelper.initialize(requireContext())
        updateFCMToken()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            val isGPSEnable=Utils.checkIfGpsEnabled(requireContext())
            if (isGPSEnable){
                fetchLocation()
            }else{
                Toast.makeText(requireContext(), "GPS is required! Please enable location.", Toast.LENGTH_LONG).show()
                openLocationSettings()
            }
        }
        viewModel.bookingStatus.observe(viewLifecycleOwner) { status ->
            if (status.first) {
                getBookings()
            }
        }
        orderList = ArrayList()

        rc_showBookings.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        ordersAdapter = BookingOrdersAdapter(emptyList(), requireContext(),viewModel)
        rc_showBookings.adapter = ordersAdapter

        getBookings()
    }

    private fun fetchLocation() {
        LocationHelper.getLastKnownLocation(requireContext()) { lat, lon ->
            Constants.LATITUDE=lat
            Constants.LONGITUDE=lon
          val address=  LocationHelper.getAddressFromLatLng(requireContext(),lat,lon)
            tv_location.text=address
             Log.e("Location","Latitude: $lat, Longitude: $lon")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            } else {
                Toast.makeText(requireContext(), "permission denied, Location permission is required!", Toast.LENGTH_LONG).show()
                openLocationSettings()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Location Alert")
            .setMessage("Location is required! Please enable your location.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Close the dialog
            }


        // Create and show the AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()

    }
    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        locationSettingsLauncher.launch(intent)
    }


    private val locationSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK || Utils.checkIfGpsEnabled(requireContext())) {
                fetchLocation() // Call location fetching when GPS is enabled
            } else {
                Toast.makeText(requireContext(), "GPS is still disabled!", Toast.LENGTH_LONG).show()
            }
        }

    private fun getBookings(){
        viewModel.getAllBookingOrders()
        viewModel.bookingOrders.observe(viewLifecycleOwner, Observer { data ->
            run {
                orderList.clear()
                if (data != null) {
                    orderList.addAll(data)
                }

                ordersAdapter.updateItems(orderList)
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid ?: ""
                sendNotificationToUser(userId = userId )

            }
        })
    }


    fun updateFCMToken() {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid ?: return

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result

                // Save the FCM token in Realtime Database
                val database = FirebaseDatabase.getInstance()
                val userRef = database.getReference("ProfessionalDetails").child(userId)

                userRef.child("fcmToken").setValue(token)
                    .addOnSuccessListener { Log.d("FCM", "Token updated for userId: $userId") }
                    .addOnFailureListener { Log.e("FCM", "Error updating token", it) }
            }
    }



    fun sendNotificationToUser(userId: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("ProfessionalDetails").child(userId)

        userRef.child("fcmToken").get().addOnSuccessListener { snapshot ->
            val token = snapshot.value as? String
            if (token != null) {
                // Send notification to the FCM Token
                sendFCMNotification(token)
            } else {
                Log.e("FCM", "FCM Token not found for userId: $userId")
            }
        }.addOnFailureListener {
            Log.e("FCM", "Error fetching FCM Token", it)
        }
    }

    fun sendFCMNotification(token: String) {
        val message = mapOf(
            "to" to token,
            "notification" to mapOf(
                "title" to "Reminder",
                "body" to "Don't forget to mark your attendance at 9:00 AM!"
            )
        )

        // Send notification via Firebase Cloud Messaging (via HTTP request)
        // Note: You need a server key for this part to send notifications programmatically.
        val url = "https://fcm.googleapis.com/fcm/send"
        val jsonObject = JSONObject(message)

        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                Log.d("FCM", "Notification sent successfully: $response")
            },
            Response.ErrorListener { error ->
                Log.e("FCM", "Failed to send notification", error)
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["Authorization"] = "key="+getString(R.string.API_KEY) // Replace with your FCM Server Key
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonRequest)
    }



}