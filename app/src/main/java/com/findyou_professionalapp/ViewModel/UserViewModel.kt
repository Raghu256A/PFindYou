package com.findyou_professionalapp.ViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findyou_professionalapp.DataClass.BookingData
import com.findyou_professionalapp.DataClass.ChatContact
import com.findyou_professionalapp.DataClass.ProfessionalsData
import com.findyou_professionalapp.DataClass.Services
import com.findyou_professionalapp.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository) : ViewModel() {
    private val _loginStatus = MutableLiveData<Pair<Boolean, String?>>()
    val loginStatus: LiveData<Pair<Boolean, String?>> = _loginStatus
    private val _userStatus = MutableLiveData<ProfessionalsData?>()
    val userStatus: LiveData<ProfessionalsData?> = _userStatus
    private val _services = MutableLiveData<List<Services>?>()
    val service: LiveData<List<Services>?> = _services

    private val _chatContStatus = MutableLiveData<ArrayList<ChatContact>?>()
    val chatContStatus: LiveData<ArrayList<ChatContact>?> = _chatContStatus
    private val _bookingOrders = MutableLiveData<ArrayList<BookingData>?>()
    val bookingOrders: LiveData<ArrayList<BookingData>?> = _bookingOrders
    private val _bookingStatus = MutableLiveData<Pair<Boolean, String?>>()
    val bookingStatus: LiveData<Pair<Boolean, String?>> = _bookingStatus

    fun registerUser(user: ProfessionalsData,profile:Uri?) {
        viewModelScope.launch {
            repository.registerUser(user,profile) { success, message ->
                _loginStatus.postValue(Pair(success, message) as Pair<Boolean, String?>?)
            }
        }

    }

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            repository.loginWithEmail(email, password) { success, messsage ->
                _loginStatus.postValue(Pair(success, messsage))
            }
        }

    }
    fun getUserData(userId: String) {
        viewModelScope.launch {
            _userStatus.value = repository.getUserData(userId)
        }
    }

    fun getServices(context: Context) {
        viewModelScope.launch {
            val list = repository.getServices()

            if (list.isNullOrEmpty()){
                insertServicesFromJson(context)
                getServices(context)
            }else{
                _services.value = list

            }
        }
    }

    fun insertServicesFromJson(context: Context) {
        viewModelScope.launch {
            try {
                repository.insertServicesFromJson(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getChatData(uId: String?){
        viewModelScope.launch {
            try {
                _chatContStatus.value = repository.getChatIds(uId)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }
    fun getAllBookingOrders() {
        val userId:String=FirebaseAuth.getInstance().currentUser!!.uid

        viewModelScope.launch {
            _bookingOrders.value= repository.getAllBookingOrders(userId)
        }

    }
    fun updateBookingStatus(bookingId:String,status:String) {

        viewModelScope.launch {
            repository.updateBookingStatus(bookingId,status) { success, message ->
                _bookingStatus.postValue(Pair(success, message) as Pair<Boolean, String?>?)
            }
        }

    }
}