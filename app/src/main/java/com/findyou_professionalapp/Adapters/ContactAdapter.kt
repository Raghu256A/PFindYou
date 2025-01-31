package com.findyou_professionalapp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.findyou_professionalapp.DataClass.ChatContact
import com.findyou_professionalapp.activities.ChatViewActivity
import com.findyou_professionalapp.DataClass.ChatData
import com.findyou_professionalapp.R
import com.google.android.material.imageview.ShapeableImageView

class ContactAdapter(
    private val context: Context,
    private var contactList: ArrayList<ChatContact>
) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {


    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var img_profile: ShapeableImageView = view.findViewById(R.id.img_profile)
        var tv_Name: TextView = view.findViewById(R.id.tv_Name)
        var tv_time: TextView = view.findViewById(R.id.tv_time)
        var tv_lastMsg: TextView = view.findViewById(R.id.tv_lastMsg)
        var tick1: View = view.findViewById(R.id.tick1)
        var tick2: View = view.findViewById(R.id.tick2)
        var ll_header: LinearLayout = view.findViewById(R.id.ll_header)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.tv_Name.text = contact.fullName
        holder.tv_time.text = contact.sendTime
        holder.tick1.visibility = View.GONE
        holder.tick2.visibility = View.GONE
        if (contact.IsSend == "1") {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ticks)
            if (contact.sendStatus == "S") {
                holder.tick1.visibility = View.VISIBLE
                holder.tick2.visibility = View.GONE
                drawable?.let {
                    DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.gray_lite))
                    holder.tick1.background = it
                }
            } else if (contact.sendStatus == "D") {
                holder.tick1.visibility = View.VISIBLE
                holder.tick2.visibility = View.VISIBLE
                drawable?.let {
                    DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.gray_lite))
                    holder.tick1.background = it
                    holder.tick1.background = it
                }

            } else if (contact.sendStatus == "V") {
                holder.tick1.visibility = View.VISIBLE
                holder.tick2.visibility = View.VISIBLE
                drawable?.let {
                    DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.primary))
                    holder.tick1.background = it
                    holder.tick1.background = it
                }

            }
            holder.tv_lastMsg.text = contact.sendMsg
        } else {
            holder.tick1.visibility = View.GONE
            holder.tick2.visibility = View.GONE
            holder.tv_lastMsg.text = contact.receiveMsg


        }

        if (!contact.profileImageUrl.isNullOrEmpty()) {
            contact.profileImageUrl.let { url ->
                Glide.with(context).load(url)
                    .into(holder.img_profile)

            }
        }
        holder.ll_header.setOnClickListener(View.OnClickListener {
            val intent = Intent(context,ChatViewActivity::class.java)
            intent.putExtra("name",contact.fullName)
            intent.putExtra("uid", contact.uid)
            intent.putExtra("phone",contact.phoneNumber)
            intent.putExtra("profileURL",contact.profileImageUrl)
            context.startActivity(intent)

        })
    }
    fun updateItems(newItems: ArrayList<ChatContact>) {
        contactList = newItems
        notifyDataSetChanged()
    }
}