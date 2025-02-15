package com.findyou_professionalapp.common

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Context.TELEPHONY_SERVICE
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.findyou_professionalapp.DataClass.ServiceType
import com.findyou_professionalapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.Format
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object Utils {




        var f_Calendar = Calendar.getInstance()

        fun showAlertDialog(context: Context, title: String, msg: String) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss() // Close the dialog
                }


            // Create and show the AlertDialog
            val alertDialog = builder.create()
            alertDialog.show()

        }

        fun togglePasswordVisibility(
            isPasswordVisible: Boolean,
            et_password: TextInputEditText
        ): Boolean {
            if (isPasswordVisible) {
                // Hide password
                et_password.transformationMethod = PasswordTransformationMethod.getInstance()
                et_password.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.eye_off,
                    0
                ) // Change drawable to eye off
            } else {
                // Show password
                et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                et_password.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.eye_on,
                    0
                ) // Change drawable to eye on
            }
            et_password.text?.let { et_password.setSelection(it.length) } // Move cursor to end
            return !isPasswordVisible
        }

        fun isMobileValid(mobileNo: String, type: String): String? {
            var alert: String? = ""

            if (mobileNo.length != 10) {
                alert += "Please enter 10 digits $type mobile number \n"
            } else if (!mobileNo.startsWith("9") && !mobileNo.startsWith("8")
                && !mobileNo.startsWith("7") && !mobileNo.startsWith("6")
            ) {
                alert += "Please enter valid $type mobile number \n"
            }

            return alert
        }

        fun isPasswordValid(password: String, type: String): String? {
            var alert: String? = ""
            val hasUpperCase = Regex("[A-Z]")
            val hasLowerCase = Regex("[a-z]")
            val hasDigit = Regex("[0-9]")
            val hasSpecialChar = Regex("[!@#$%^&*?]")
            if (password.length < 6) {
                alert += type + "Password should above 5 characters \n"
            } else if (!hasUpperCase.containsMatchIn(password)) {
                alert += type + "Password should min one upper latter required \n"
            } else if (!hasUpperCase.containsMatchIn(password)) {
                alert += type + "Password should min one upper latter required \n"
            } else if (!hasLowerCase.containsMatchIn(password)) {
                alert += type + "Password should min one lower latter required \n"
            } else if (!hasDigit.containsMatchIn(password)) {
                alert += type + "Password should min one digit required \n"
            } else if (!hasSpecialChar.containsMatchIn(password)) {
                alert += type + "Password should min one specialChar required like !@#\$%^&*? \n"
            }

            return alert
        }

        fun isValidEmail(email: String): String {
            val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
            if (!emailRegex.matches(email)) {
                return "Invalid email address.\n"
            }
            return ""
        }

        fun openDialog(context: Context): ProgressDialog {
            var dialog: ProgressDialog? = null
            dialog = ProgressDialog(context).apply {
                setMessage("Please wait...")
                setCancelable(false)
                show()
            }

            return dialog
        }

        fun closeDialog(dialog: ProgressDialog) {
            if (dialog != null && dialog.isShowing) {
                dialog.dismiss()
            }

        }

        fun isNullOrEmpty(value:String?): String? {
            var str:String?="";
            if (value.isNullOrEmpty()){
              return str
            }
            return value

        }
        fun isLoginWithEmail(userId: String):Boolean?{
            var value:Boolean=false
            val hasCharU = Regex("[a-z]")
            val hasCharL = Regex("[A-Z]")

            if(hasCharU.containsMatchIn(userId)||hasCharL.containsMatchIn(userId)) {
                value=true
            }
            return value
        }
        fun getDate(): String? {
            val c = Calendar.getInstance()
            return c[Calendar.DAY_OF_MONTH]
                .toString() + " " + (c[Calendar.MONTH] + 1) + " " + c[Calendar.YEAR]
        }

        fun stringToDateTimeDefault(dateTime: String?, format: String?): Date {
            var result: Date? = null
            try {
                if (dateTime != null) {
                    val sdf = SimpleDateFormat(format, Locale.ENGLISH)
                    result = sdf.parse(dateTime)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result!!
        }

        fun getDateToString(date: Date?, formats: String?): String? {
            var date = date
            var result = ""
            try {
                if (date == null) {
                    date = f_Calendar.getTime()
                }
                val dateFormat: Format = SimpleDateFormat(formats, Locale.ENGLISH)
                result = dateFormat.format(date)
            } catch (e: java.lang.Exception) {
                // e.getMessage(), e);
            }
            return result
        }


        fun getMonthNumber(month: Int): String {
            var strmonth = ""
            if (month == 0) {
                strmonth = "01"
            }
            if (month == 1) {
                strmonth = "02"
            }
            if (month == 2) {
                strmonth = "03"
            }
            if (month == 3) {
                strmonth = "04"
            }
            if (month == 4) {
                strmonth = "05"
            }
            if (month == 5) {
                strmonth = "06"
            }
            if (month == 6) {
                strmonth = "07"
            }
            if (month == 7) {
                strmonth = "08"
            }
            if (month == 8) {
                strmonth = "09"
            }
            if (month == 9) {
                strmonth = "10"
            }
            if (month == 10) {
                strmonth = "11"
            }
            if (month == 11) {
                strmonth = "12"
            }
            return strmonth
        }

        fun getDayNumber(month: Int): String {
            var strmonth = ""
            strmonth = if (month == 1) {
                "01"
            } else if (month == 2) {
                "02"
            } else if (month == 3) {
                "03"
            } else if (month == 4) {
                "04"
            } else if (month == 5) {
                "05"
            } else if (month == 6) {
                "06"
            } else if (month == 7) {
                "07"
            } else if (month == 8) {
                "08"
            } else if (month == 9) {
                "09"
            } else {
                month.toString()
            }
            return strmonth
        }

        fun getMonth(month: Int): String? {
            var strmonth:String? = ""
            if (month == 0) {
                strmonth = "Jan"
            }
            if (month == 1) {
                strmonth = "Feb"
            }
            if (month == 2) {
                strmonth = "Mar"
            }
            if (month == 3) {
                strmonth = "Apr"
            }
            if (month == 4) {
                strmonth = "May"
            }
            if (month == 5) {
                strmonth = "Jun"
            }
            if (month == 6) {
                strmonth = "Jul"
            }
            if (month == 7) {
                strmonth = "Aug"
            }
            if (month == 8) {
                strmonth = "Sep"
            }
            if (month == 9) {
                strmonth = "Oct"
            }
            if (month == 10) {
                strmonth = "Nov"
            }
            if (month == 11) {
                strmonth = "Dec"
            }
            return strmonth
        }

        fun getDateOnly(): String {
            var st_date = ""
            try {
                val DateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                val c = Calendar.getInstance()
                st_date = DateFormat.format(c.time)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return st_date
        }




    fun convertMapToJson(map: Map<String, Any?>): String {
        val gson = Gson()
        return gson.toJson(map)
    }


    fun convertJsonToMap(json: String): Map<String, Any?> {
        val gson = Gson()
        val type = object : TypeToken<Map<String, Any?>>() {}.type
        // Convert JSON string to Map
        return gson.fromJson(json, type)
    }
    fun getIMEI(context: Context):String{
        var imeiNumber:String=""

        try{
            val telephoneManager= context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            imeiNumber = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                telephoneManager.imei// For Android Oreo and above
            } else {
                telephoneManager.deviceId  // For older Android versions
            }

        }catch (e:Exception){
            e.printStackTrace()
            imeiNumber = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        }
        return imeiNumber
    }

    fun isTokenValid(token: String,context: Context): Boolean {
        val  imei=getIMEI(context)

        return token==imei
    }
    fun saveLoginDevice(context: Context,id:String){
        try{
            val sharedPreferences = context.getSharedPreferences("userPrefs", MODE_PRIVATE)
            val imei = getIMEI(context)
            val editor = sharedPreferences.edit()
            editor.putString("authToken", imei)
            editor.putString(imei,id)
            editor.apply()
        }catch (e:Exception){
            e.printStackTrace()
        }


    }
    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Format for time in AM/PM
        return sdf.format(Date())
    }
    fun printLogcat(e:Exception, tag:String){
        Log.e(tag, " Exception occurred: ${e.message}", e)

    }
    fun isNetworkAvailable(context: Context): Boolean {
        var isNetworkAvailable = false
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable && activeNetworkInfo.isConnected) {
                isNetworkAvailable = true
            }
        } catch (e: Exception) {
            // Log the exception
            printLogcat(e, "isNetworkAvailable")
        }
        return isNetworkAvailable
    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        val distance=results[0]
        val formattedNumber = String.format("%.2f", distance / 1000)

        return "${formattedNumber} KM"
    }

     fun checkIfGpsEnabled(context: Context) :Boolean{
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

       return isGpsEnabled
    }
    fun stringToDouble(value: String?):Double{
        var default=0.0
        if (!value.isNullOrEmpty()){
            default=value.toDouble()
        }
        return default
    }
    fun sanitizeBookingId(bookingId: String): String {
        return bookingId.replace("[#\\$\\.\\[\\]:]".toRegex(), "_")  // Replaces invalid characters with "_"
    }
}