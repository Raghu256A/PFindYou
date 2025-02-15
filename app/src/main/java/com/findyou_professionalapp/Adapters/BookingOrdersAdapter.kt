package com.findyou_professionalapp.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.findyou_professionalapp.DataClass.BookingData
import com.findyou_professionalapp.R
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.activities.ChatViewActivity
import com.findyou_professionalapp.common.Constants
import com.findyou_professionalapp.common.Utils


class BookingOrdersAdapter(private var bookingList: List<BookingData>, private val contexts: Context,private val viewModel: UserViewModel) :
    RecyclerView.Adapter<BookingOrdersAdapter.OrdersViewHolder>() {

        class OrdersViewHolder(view: View):RecyclerView.ViewHolder(view){
            var tv_Name=view.findViewById<TextView>(R.id.tv_Name)
            var tv_BId=view.findViewById<TextView>(R.id.tv_BId)
            var tv_serviceName=view.findViewById<TextView>(R.id.tv_serviceName)
            var tv_Distance=view.findViewById<TextView>(R.id.tv_Distance)
            var tv_location=view.findViewById<TextView>(R.id.tv_location)
            var btn_accept=view.findViewById<Button>(R.id.btn_accept)
            var btn_Decline=view.findViewById<Button>(R.id.btn_Decline)
            var btn_ViewMap=view.findViewById<Button>(R.id.btn_ViewMap)
            var btn_Reschedule=view.findViewById<Button>(R.id.btn_Reschedule)
            var btn_chat=view.findViewById<Button>(R.id.btn_chat)
            var ll_header=view.findViewById<CardView>(R.id.ll_header)
            var ll_AcceptStatus=view.findViewById<LinearLayout>(R.id.ll_AcceptStatus)
            var ll_Buttons=view.findViewById<LinearLayout>(R.id.ll_Buttons)

        }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
      val view:View =LayoutInflater.from(parent.context).inflate(R.layout.booking_manage_view_list,parent,false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order:BookingData = bookingList[position]

        holder.tv_Name.text=order.costumerName
        holder.tv_serviceName.text=order.bookingServiceType
        val bookingId=order.bookingID
        holder.tv_BId.text=bookingId

        (order.fullAddress+" "+order.villageArea+" "+
                order.landMark+" "+order.pinCode+" "+order.district+" "+
                order.state+" "+order.country).also { holder.tv_location.text = it }
        val lat =Utils.stringToDouble(order.lat)
        val log =Utils.stringToDouble(order.log)

        if (order.bookingStatus.equals("accept")){
            holder.ll_AcceptStatus.visibility=View.VISIBLE
            holder.ll_Buttons.visibility=View.GONE
            holder.ll_header.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#f8ecbd"))
        }else{
            holder.ll_AcceptStatus.visibility=View.GONE
            holder.ll_Buttons.visibility=View.VISIBLE
            holder.ll_header.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#E9EBF8"))


        }

        holder.tv_Distance.text=Utils.calculateDistance(Constants.LATITUDE,Constants.LONGITUDE,lat,log)
        handleButtonClicks(holder, order)



    }
    private fun handleButtonClicks(holder: OrdersViewHolder, order: BookingData) {



        holder.btn_accept.setOnClickListener {
            viewModel.updateBookingStatus((order.Id!!), "accept")
        }

        holder.btn_Reschedule.setOnClickListener {
            viewModel.updateBookingStatus((order.Id!!), "reschedule")
        }

        holder.btn_Decline.setOnClickListener {
            viewModel.updateBookingStatus((order.Id!!), "decline")
        }

        holder.btn_chat.setOnClickListener {
            val intent = Intent(contexts, ChatViewActivity::class.java)
            intent.putExtra("name",order.costumerName)
            intent.putExtra("uid", order.costumerID)
            intent.putExtra("phone",order.mobileNumber)
            intent.putExtra("profileURL","")
            contexts.startActivity(intent)
        }
    }

    fun updateItems(newItems: List<BookingData>) {
        bookingList = newItems
        notifyDataSetChanged()
    }
}