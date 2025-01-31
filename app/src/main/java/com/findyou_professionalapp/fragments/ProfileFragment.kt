package com.findyou_professionalapp.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.findyou_professionalapp.DataClass.ProfessionalsData
import com.findyou_professionalapp.R
import com.findyou_professionalapp.ViewModel.UserViewModel
import com.findyou_professionalapp.activities.Sign_in
import com.findyou_professionalapp.common.Constants
import com.findyou_professionalapp.common.Utils
import com.findyou_professionalapp.repository.Repository
import com.findyou_professionalapp.viewModelFactory.UserViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {

    private val REQUEST_CODE_CAMERA = 100
    private val REQUEST_CODE_GALLERY = 101
    private val userViewModel: UserViewModel
            by viewModels {
                val userRepository = Repository()
                UserViewModelFactory(userRepository)
            }
    private lateinit var imgProfile: ShapeableImageView
    private lateinit var tv_name: TextInputEditText
    private lateinit var tv_phone: TextInputEditText
    private lateinit var tv_email: TextInputEditText
    private lateinit var imv_setting: ImageView
    private lateinit var progress_bar: ProgressBar
    private lateinit var et_Gender: TextInputEditText
    private lateinit var et_DOB: TextInputEditText
    private lateinit var bnt_Update_pic: ImageButton
    private var profilePicUri: Uri?=null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_profile, container, false)
        updateXML(view)

        return view
    }
    private fun updateXML(view: View){
        imgProfile = view.findViewById(R.id.img_profile)
        tv_name = view.findViewById(R.id.et_Name)
        tv_email = view.findViewById(R.id.et_email)
        tv_phone = view.findViewById(R.id.et_mobileNumber)
        imv_setting =view.findViewById(R.id.imv_setting)
        et_Gender=view.findViewById(R.id.et_Gender)
        et_DOB= view.findViewById(R.id.et_DOB)
        bnt_Update_pic= view.findViewById(R.id.bnt_Update_pic)
        drawerLayout = view.findViewById(R.id.main)
        navView = view.findViewById(R.id.navView)
        progress_bar = view.findViewById(R.id.progress_bar)
        bnt_Update_pic.setOnClickListener ( View.OnClickListener {
            selectImageSource();
        })
        getUserData(Constants.UserID)
        imv_setting.setOnClickListener(View.OnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)

        })
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logOut -> {
                    logout()
                }

            }
            drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
    }
    private fun getUserData(userId:String){
        progress_bar.visibility=View.VISIBLE
        userViewModel.getUserData(userId)
        userViewModel.userStatus.observe(viewLifecycleOwner){user->
            user?.let {
                displayUserData(it)
            }
            progress_bar.visibility=View.GONE
        }
    }

    private fun displayUserData(userDo: ProfessionalsData){
        progress_bar.visibility=View.GONE

        tv_name.setText(userDo.name)
        tv_phone.setText(userDo.phoneNumber)
        tv_email.setText(userDo.emailID)
        if(userDo.gender.equals("M")){
            et_Gender.setText("Male")
        }else{
            et_Gender.setText("Female")
        }

        et_DOB.setText(userDo.userDOB)
        if(!userDo.profileImageUrl.isNullOrEmpty()){
            userDo.profileImageUrl.let { url->
                Glide.with(requireActivity()).load(url)
                    .into(imgProfile)

            }
        }



    }
    private fun selectImageSource() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Image Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }
    private fun saveBitmapToFile(bitmap: Bitmap): Uri? {
        val file = File(requireActivity().cacheDir, "profile_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return Uri.fromFile(file)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    profilePicUri = saveBitmapToFile(imageBitmap)
                }
                REQUEST_CODE_GALLERY -> {
                    profilePicUri = data?.data

                }
            }
            if (profilePicUri !=null){
                imgProfile.setImageURI(profilePicUri)

            }
        }

    }
    fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("userPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", null)
        val editor = sharedPreferences.edit()
        editor.remove("authToken")
        editor.remove(token)
        editor.apply()
        val intent = Intent(requireContext(), Sign_in::class.java)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }
}