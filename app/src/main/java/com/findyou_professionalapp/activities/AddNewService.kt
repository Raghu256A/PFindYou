package com.findyou_professionalapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.findyou_professionalapp.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject

class AddNewService : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_services)
        sendNotification()
    }


    private fun sendNotification(){
       try {
            val senderTockenID: String = "cFUFUx7aSiakC_GOHwKGwL:APA91bFKWVEqxdGfLglwlzIdSpFy3FSuQ2kEUyQDE7TVvZuW_XSWhC-rWYioa64kw26Ff9mRqsxzrK_HeuB4aHQkRm5MbEX9AWvgMQe63oE5DFieJiUyJEA"
           val client: OkHttpClient = OkHttpClient()
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
            val jsonObject = JSONObject()
            val jsonObjectMain = JSONObject()
            jsonObject.put("body", "hi Raghu")
            jsonObject.put("title", "Alert")
            jsonObjectMain.put("to", senderTockenID)
            jsonObjectMain.put("notification", jsonObject)

            val requestBody = RequestBody.create(mediaType, jsonObjectMain.toString())
            val request = Request.Builder().url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .addHeader("Authorization", "key=" + getString(R.string.API_KEY))
                .addHeader("Content-Type", "application/json").build()
            val response = client.newCall(request).execute()

            println(response.toString())

        }catch (e:Exception){
           println(e.message.toString())

       }

    }
}