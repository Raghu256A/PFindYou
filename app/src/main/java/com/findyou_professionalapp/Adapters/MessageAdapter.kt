package com.findyou_professionalapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.findyou_professionalapp.DataClass.Message
import com.findyou_professionalapp.R

import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            // inflate receive
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.receive_msg, parent, false)
            return ReceiveViewHolder(view)
        }else{
            // inflate sent
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sent_msg, parent, false)
            return SentViewHolder(view)

        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            //sent message data
            val viewHolder = holder as SentViewHolder
            holder.sentMsg.text = currentMessage.message
            holder.sentTime.text = currentMessage.sendTime
            if (currentMessage.message!!.isNotEmpty()) {
                val drawable = ContextCompat.getDrawable(context, R.drawable.ticks)
                if (currentMessage.messageStatus == "S") {
                    holder.tick1.visibility = View.VISIBLE
                    holder.tick2.visibility = View.GONE
                    drawable?.let {
                        DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.gray_lite))
                        holder.tick1.background = it
                    }
                } else if (currentMessage.messageStatus == "D") {
                    holder.tick1.visibility = View.VISIBLE
                    holder.tick2.visibility = View.VISIBLE
                    drawable?.let {
                        DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.gray_lite))
                        holder.tick1.background = it
                        holder.tick1.background = it
                    }

                } else if (currentMessage.messageStatus == "V") {
                    holder.tick1.visibility = View.VISIBLE
                    holder.tick2.visibility = View.VISIBLE
                    drawable?.let {
                        DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.green))
                        holder.tick1.background = it
                        holder.tick1.background = it
                    }

                }
            } else {
                holder.tick1.visibility = View.GONE
                holder.tick2.visibility = View.GONE

            }


        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMsg.text = currentMessage.message
            holder.receiveTime.text = currentMessage.sendTime
            if (currentMessage.message!!.isNotEmpty()) {
                val drawable = ContextCompat.getDrawable(context, R.drawable.ticks)
                if (currentMessage.messageStatus== "S") {
                    holder.tick1.visibility = View.VISIBLE
                    holder.tick2.visibility = View.GONE
                    drawable?.let {
                        DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.gray_lite))
                        holder.tick1.background = it
                    }
                } else if (currentMessage.messageStatus == "D") {
                    holder.tick1.visibility = View.VISIBLE
                    holder.tick2.visibility = View.VISIBLE
                    drawable?.let {
                        DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.gray_lite))
                        holder.tick1.background = it
                        holder.tick1.background = it
                    }

                } else if (currentMessage.messageStatus == "V") {
                    holder.tick1.visibility = View.VISIBLE
                    holder.tick2.visibility = View.VISIBLE
                    drawable?.let {
                        DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.green))
                        holder.tick1.background = it
                        holder.tick1.background = it
                    }

                }
            } else {
                holder.tick1.visibility = View.GONE
                holder.tick2.visibility = View.GONE


            }


        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sentMsg:TextView = view.findViewById<TextView>(R.id.tv_sent_msg)
        val sentTime:TextView = view.findViewById<TextView>(R.id.tv_sent_time)
        val tick1:View = view.findViewById<TextView>(R.id.tick1)
        val tick2:View = view.findViewById<TextView>(R.id.tick2)


    }

    class ReceiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val receiveMsg: TextView = view.findViewById<TextView>(R.id.tv_receive_msg)
        val receiveTime:TextView = view.findViewById<TextView>(R.id.tv_receive_time)
        val tick1:View = view.findViewById<TextView>(R.id.tick1)
        val tick2:View = view.findViewById<TextView>(R.id.tick2)


    }
}