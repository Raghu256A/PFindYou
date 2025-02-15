package com.findyou_professionalapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import com.findyou_professionalapp.DataClass.BookingData
import com.findyou_professionalapp.DataClass.ChatContact
import com.findyou_professionalapp.DataClass.ProfessionalsData
import com.findyou_professionalapp.DataClass.ServiceType
import com.findyou_professionalapp.DataClass.Services
import com.findyou_professionalapp.common.Constants
import com.findyou_professionalapp.common.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.io.InputStream

class Repository {
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    suspend fun registerUser(
        user: ProfessionalsData,
        profile:Uri?,
        callback: (Boolean?, String?) -> Unit
    ) {
        user.emailID?.let {
            user.password?.let { it1 ->
                auth.createUserWithEmailAndPassword(it, it1)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser!!.uid
                            user.uid=userId
                            if (profile!=null){
                                uploadProfilePic(profile, user.name!!) { imageUri ->
                                    if (imageUri != null) {
                                        saveUserData(
                                            userId,
                                            user.copy(profileImageUrl = imageUri),
                                            callback
                                        )
                                    } else {
                                        callback(false, "Profile pic upload failed try again..!")
                                    }
                                }
                            }else{
                                saveUserData(
                                    userId,
                                    user.copy(profileImageUrl = "https://firebasestorage.googleapis.com/v0/b/eoshopping-eb325.appspot.com/o/Raghu_profilePic%2F1732108363999.jpg?alt=media&token=7913747d-c8b9-4c64-b624-0c663019530e"),
                                    callback
                                )
                            }

                        }
                    }
            }
        }
    }

    private fun saveUserData(userId: String?, user: ProfessionalsData, callback: (Boolean, String?) -> Unit) {
        if (userId != null) {
            db.getReference("ProfessionalDetails").child(userId).setValue(user)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message)
                }
        } else {
            callback(false, "User Id is null try again...!")
        }
    }

    suspend fun loginWithEmail(
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful, task.exception?.message)
            }
    }

    suspend fun insertServicesFromJson(context: Context) {
        try {
            val jsonString = readJsonFromAssets("services.json", context)
            val jsonObject = JSONObject(jsonString)
            val servicesMap = mutableMapOf<String, Services>()

            // Parse JSON into a Map
            val servicesJson = jsonObject.getJSONObject("Services")
            servicesJson.keys().forEach { key ->
                val serviceJson = servicesJson.getJSONObject(key)
                val typesJson = serviceJson.getJSONObject("types")
                val typesMap = mutableMapOf<String, ServiceType>()

                typesJson.keys().forEach { typeKey ->
                    val subtype = typesJson.getJSONArray(typeKey).let {
                        List(it.length()) { i ->
                            it.getString(i)
                        }
                    }
                    typesMap[typeKey] = ServiceType(typeKey, subtype)
                }

                val service = Services(
                    name = serviceJson.getString("name"),
                    description = serviceJson.getString("description"),
                    types = typesMap
                )
                servicesMap[key] = service
            }

            firestore.collection("Services")
                .document("services_data")
                .set(servicesMap)
                .await()
        } catch (e: Exception) {
            throw Exception("Error inserting services data: ${e.message}")
        }
    }

    // Read JSON from assets folder
    private fun readJsonFromAssets(fileName: String, context: Context): String {
        val inputStream: InputStream = context.assets.open(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }

    fun uploadProfilePic(imageUri: Uri, userName: String, callback: (String?) -> Unit) {
        val file =
            storage.reference.child(userName + "_profilePic/${System.currentTimeMillis()}.jpg")
        file.putFile(imageUri).addOnSuccessListener {
            file.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString())
            }.addOnFailureListener {
                callback(null)
            }
        }.addOnFailureListener {
            callback(null)
        }
    }
    suspend fun getServices(): List<Services>? {
        val firestore = FirebaseFirestore.getInstance()
        return try {
            val snapshot = firestore.collection("Services").
            document("services_data").get().await()

            snapshot.data?.map { entry ->
                val serviceData = entry.value as? Map<String, Any>
                val name = serviceData?.get("name") as? String ?: ""
                val description = serviceData?.get("description") as? String ?: ""
                val typesData = serviceData?.get("types") as? Map<String, Any> ?: emptyMap()

                // Parse types and their subtypes
                val typesMap = typesData.mapValues { (typeKey, typeValue) ->
                    val typeData = typeValue as? Map<String, Any> ?: emptyMap()
                    val typeName = typeData["name"] as? String ?: ""
                    val subtypes = typeData["subtypes"] as? List<String> ?: emptyList()
                    ServiceType(name = typeName, subtypes = subtypes)

                }

                Services(name = name, description = description, types = typesMap)
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e("ServiceFetch", "Error fetching services: ${e.message}")
            null
        }
    }
    suspend fun getUserData(userId: String?): ProfessionalsData? {
        return try {
            if (Constants.IsMobile) {
                val reference =
                    db.getReference("ProfessionalDetails").orderByChild("phoneNumber").equalTo(userId)
                        .get().await()
                if (reference.exists()) {
                    val userMap = reference.children.first().getValue(ProfessionalsData::class.java)
                    userMap
                } else {
                    null // User not found
                }
            } else {
                val reference =
                    db.getReference("ProfessionalDetails").orderByChild("emailID").equalTo(userId).get()
                        .await()
                if (reference.exists()) {
                    val userMap = reference.children.first().getValue(ProfessionalsData::class.java)
                    userMap
                } else {
                    null // User not found
                }

            }


        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    suspend fun getChatIds(uId: String?): ArrayList<ChatContact>? {

        var list:ArrayList<ChatContact>
        try {
            list= ArrayList()
            val chatIds = mutableListOf<String>()
            val chats = db.getReference("chats").get().await()
            if (chats.exists()){
                list.clear()
                for (chatSnapshot in chats.children) {
                    var chatId: String = chatSnapshot.key.toString()
                    if (chatId.contains(uId!!)){
                        chatId = chatId.replace(uId, "")
                        if (!chatIds.contains(chatId)) {
                            chatIds.add(chatId)
                        }
                    }

                }

                list=  getChatContact(chatIds)

            }


        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return list
    }

    private suspend fun getChatContact(chatIds: List<String>?): ArrayList<ChatContact> {

        val profList = ArrayList<ChatContact>()

        try {
            if (chatIds!!.isNotEmpty()) {
                for (profId in chatIds) {
                    val res = db.getReference("UserDetails")/*.orderByChild("uid").equalTo(profId)*/
                        .get()
                        .await()

                    if (res.exists()) {
                        for (userSnapshot in res.children) {
                            val prof = userSnapshot.getValue(ChatContact::class.java)
                            if (prof != null &&userSnapshot.key.equals(profId)) {
                                prof.uid=userSnapshot.key
                                profList.add(prof)
                            }
                        }
                    }

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return profList
    }
    suspend fun getAllBookingOrders(userId:String):ArrayList<BookingData>?{
        return try {
            val list:ArrayList<BookingData> = ArrayList()

            val reference =
                db.getReference("BookingData").orderByChild("profUsID").equalTo(userId)
                    .get().await()
            if (reference.exists()) {
                list.clear()
                reference.children.forEach { data ->
                    val bookingData = data.getValue(BookingData::class.java)
                    val dataId=data.key
                    if (bookingData != null) {
                        if (bookingData.bookingStatus.equals("pending") || bookingData.bookingStatus.equals("processing") || bookingData.bookingStatus.equals("accept")){
                            bookingData.Id=dataId
                            list.add(bookingData)
                        }

                    }
                }
                list
            } else {
                null
            }
        }catch (e:Exception){
            Utils.printLogcat(e,"getBookingDetails")
            null
        }
    }
   suspend fun updateBookingStatus(bookingId: String, newStatus: String,callback: (Boolean, String?) -> Unit) {

       db.getReference("BookingData").child(bookingId).updateChildren(
           mapOf(
               "bookingStatus" to newStatus
           )
       )
           .addOnCompleteListener { task ->
               callback(task.isSuccessful,task.exception.toString())
           }.await()
    }


}


