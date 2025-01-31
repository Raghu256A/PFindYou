package com.findyou_professionalapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.findyou_professionalapp.Adapters.MessageAdapter
import com.findyou_professionalapp.DataClass.Message
import com.findyou_professionalapp.DataClass.ProfessionalsData
import com.findyou_professionalapp.R
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.common.Utils
import com.findyou_professionalapp.repository.Repository
import com.findyou_professionalapp.viewModelFactory.UserViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatViewActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var rc_Chat: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var back: View
    private lateinit var sim_contact_dp: ShapeableImageView
    private lateinit var tv_contactName: TextView
    private lateinit var Call: View
    private lateinit var opt_menu: View
    private lateinit var attachment: View
    private lateinit var camera: View
    private lateinit var mic: View
    private lateinit var sent_btn: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private var receiverRoom: String? = null
    private var senderRoom: String? = null
    private var clientName: String? = ""
    private var userName: String? = ""
    private var  clientPhone: String? = ""
    private var userPhone: String? = ""
    private var receiverUid: String? = ""
    private var clientProfileURL: String? = ""
    private var userProfileURL: String? = ""
    private var senderUid: String? = ""
    private var isConfirm: Boolean? = false
    private lateinit var mDbRef: DatabaseReference
    private val userViewModel: UserViewModel
            by viewModels {
                val userRepository = Repository()
                UserViewModelFactory(userRepository)
            }
    private lateinit var userData:ProfessionalsData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getUserData()
        UpdateXML()
    }

    fun UpdateXML() {
        try {
            rc_Chat = findViewById(R.id.rc_Chat)
            messageBox = findViewById(R.id.et_enter_msg)
            back = findViewById(R.id.back)
            back.setOnClickListener(this)
            sim_contact_dp = findViewById(R.id.sim_contact_dp)
            tv_contactName = findViewById(R.id.tv_contactName)
            Call = findViewById(R.id.Call)
            Call.setOnClickListener(this)
            opt_menu = findViewById(R.id.opt_menu)
            opt_menu.setOnClickListener(this)
            attachment = findViewById(R.id.att)
            attachment.setOnClickListener(this)
            camera = findViewById(R.id.camera)
            camera.setOnClickListener(this)
            mic = findViewById(R.id.mic)
            mic.setOnClickListener(this)
            sent_btn = findViewById(R.id.sent_btn)
            sent_btn.setOnClickListener(this)
            messageList = ArrayList()
            clientName = intent.getStringExtra("name")
            clientPhone = intent.getStringExtra("phone")
            receiverUid = intent.getStringExtra("uid")
            clientProfileURL = intent.getStringExtra("profileURL")
            senderUid = FirebaseAuth.getInstance().currentUser?.uid
            senderRoom = receiverUid + senderUid
            receiverRoom = senderUid + receiverUid
            mDbRef = FirebaseDatabase.getInstance().getReference()



            tv_contactName.text = clientName
            if (!clientProfileURL.isNullOrEmpty()) {
                clientProfileURL.let { url ->
                    Glide.with(this).load(url)
                        .into(sim_contact_dp)

                }
            }
            messageAdapter = MessageAdapter(this, messageList)
            rc_Chat.layoutManager=LinearLayoutManager(this )
            rc_Chat.adapter=messageAdapter


            getChat()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        try {
            when (v?.id) {
                R.id.back -> {
                    onBackPressed()

                }

                R.id.att -> {

                }

                R.id.camera -> {

                }

                R.id.opt_menu -> {

                }

                R.id.Call -> {

                }

                R.id.mic -> {

                }

                R.id.sent_btn -> {
                    val message = messageBox.text.toString()
                    val time = Utils.getCurrentTime()
                    if (!message.isEmpty()) {
                        val messageObject = Message(
                            message,
                            senderUid,
                            receiverUid,
                            userPhone,
                            clientPhone,
                            "",
                            time,
                            "D",
                            userName,
                            clientName,
                            userProfileURL,
                            clientProfileURL
                        )
                        mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                            .setValue(messageObject).addOnSuccessListener {
                                mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                                    .setValue(messageObject)


                            }
                        messageBox.setText("")

                    }

                }

                else -> {
                    Toast.makeText(this, "not click", Toast.LENGTH_SHORT).show()

                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getChat(){
        try {
            mDbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(
                object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (postSnap in snapshot.children){
                            val message=postSnap.getValue(Message::class.java)
                            messageList.add(message!!)

                        }
                        messageAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    private fun getUserData() {
        userData = ProfessionalsData()
        val  userId:String=FirebaseAuth.getInstance().currentUser!!.uid
        userViewModel.getUserData(userId)
        userViewModel.userStatus.observe(this) { user ->
            user?.let {
                userName = user.name
                userPhone = user.phoneNumber
                userProfileURL = user.profileImageUrl

            }

        }
    }
}