package com.findyou_professionalapp.DataClass

import android.media.Rating
import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ProfessionalsData(
    var name: String? = "",
    var phoneNumber: String? = "",
    var emailID: String? = "",
    var gender: String? = "",
    var password: String? = "",
    var userDOB: String? = "",
    var profession:String?="",
    var rating: Int?=0,
    var reviews:String?="",
    val profileImageUrl: String? = "",
    var description: String? = "",
    var location: String? = "",
    var uid:String?="",

    )

/*data class Services(
    var name:String="",
    var description:String=""
)*/
data class Services(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("types") val types: Map<String, ServiceType> = emptyMap()
)

data class ServiceType(
    @SerializedName("name") val name: String,
    @SerializedName("subtypes") val subtypes: List<String> = emptyList()
)
data class ChatData(
    var Name: String? = "",
    var uid:String?="",
    var phoneNumber: String? = "",
    var emailID: String? = "",
    var sendTime: String? = "",
    var receiveTime: String? = "",
    var sendMsg: String? = "",
    var receiveMsg: String? = "",
    var receiveMediaURL: String? = "",
    var sendMediaURL: String? = "",
    var sendStatus: String? = "",
    var IsSend: String? = "",
    var receiveStatus: String? = "",
    var profileImageURL: String? = ""
)
data class ChatContact(
    var fullName: String? = "",
    var phoneNumber: String? = "",
    var emailID: String? = "",
    var gender: String? = "",
    var password: String? = "",
    var userDOB: String? = "",
    val profileImageUrl: String? = "",
    var senderRoomId:String?="",
    var receiverRoomId:String?="",
    var sendTime: String? = "",
    var receiveTime: String? = "",
    var sendMsg: String? = "",
    var receiveMsg: String? = "",
    var receiveMediaURL: String? = "",
    var sendMediaURL: String? = "",
    var sendStatus: String? = "",
    var IsSend: String? = "",
    var receiveStatus: String? = "",
    var uid:String?=""
    )

@Parcelize
data class BookingData(
    var profName: String? = "",
    var bookingID: String? = "",
    var profPhone: String? = "",
    var profEmail: String? = "",
    var profProfileImageUrl: String? = "",
    var profUsID: String? = "",
    var profProfession: String? = "",
    var proLocation: String? = "",
    var costumerName: String? = "",
    var mobileNumber: String? = "",
    var bookingDate: String? = "",
    var bookingTime:String?="",
    var bookingServiceType:String?="",
    var country:String?="",
    var state:String?="",
    var landMark:String?="",
    var pinCode:String?="",
    var villageArea:String?="",
    var fullAddress:String?="",
    var lat:String?="",
    var log:String?="",
    var otherInfo:String?="",
    var costumerID: String?="",
    var paymentMode: String?="",
    var minimumAmount: String?="",
    var payedAmount: String?="",
    var bookingStatus: String?="",
    var paymentID: String? = "",
    var bookedDateTime: String? = "",
    var district: String? = "",
    var Id: String? = ""
): Parcelable