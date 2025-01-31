package com.findyou_professionalapp.fragments

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.findyou_professionalapp.Adapters.ContactAdapter
import com.findyou_professionalapp.DataClass.ChatContact
import com.findyou_professionalapp.R
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.common.Utils
import com.findyou_professionalapp.repository.Repository
import com.findyou_professionalapp.viewModelFactory.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class ChatFragment : Fragment() {
    private lateinit var et_Search: EditText
    private lateinit var rc_showContacts: RecyclerView
    private lateinit var progress_bar: ProgressBar
    private lateinit var contactList:ArrayList<ChatContact>
    private lateinit var contactAdapter: ContactAdapter

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
        val view =inflater.inflate(R.layout.fragment_chat, container, false)

        updateXML(view)
        return view
    }
    private fun updateXML(view: View){
        et_Search = view.findViewById(R.id.et_Search)
        rc_showContacts = view.findViewById(R.id.rc_showContacts)
        progress_bar = view.findViewById(R.id.progress_bar)
        progress_bar.visibility=View.VISIBLE
        contactList = ArrayList()
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        viewModel.getChatData(senderUid)
        getProfessional()

    }
    private fun getProfessional() {
        try {
            contactAdapter = ContactAdapter(requireContext(), contactList)
            rc_showContacts.layoutManager =
                LinearLayoutManager(requireContext())
            rc_showContacts.adapter = contactAdapter

            viewModel.chatContStatus.observe(viewLifecycleOwner) { user ->
                user?.let {
                    contactList.clear()
                    contactList.addAll(user)
                    contactAdapter.updateItems(contactList)

                }
                progress_bar.visibility=View.GONE
            }

        } catch (e: Exception) {
            e.printStackTrace()
            progress_bar.visibility=View.GONE

        }

    }

}