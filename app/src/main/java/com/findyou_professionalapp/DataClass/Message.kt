package com.findyou_professionalapp.DataClass

class Message {
    var message: String? = ""
    var senderId: String? = ""
    var receiverId: String? = ""
    var senderPhoneNumber: String? = ""
    var receiverPhoneNumber: String? = ""
    var mediaURL: String? = ""
    var sendTime: String? = ""
    var messageStatus: String? = ""
    var senderName: String? = ""
    var receiverName: String? = ""
    var senderProfileImg: String? = ""
    var receiverProfileImg: String? = ""

    constructor() {}
    constructor(
        message: String?,
        senderId: String?,
        receiverId: String?,
        senderPhoneNumber: String?,
        receiverPhoneNumber: String?,
        mediaURL: String?,
        sendTime: String?,
        messageStatus: String?,
        senderName: String?,
        receiverName: String?,
        senderProfileImg: String?,
        receiverProfileImg: String?

    ) {
        this.message = message
        this.senderId = senderId
        this.receiverId = receiverId
        this.senderPhoneNumber = senderPhoneNumber
        this.receiverPhoneNumber = receiverPhoneNumber
        this.mediaURL = mediaURL
        this.sendTime = sendTime
        this.messageStatus = messageStatus
        this.senderName = senderName
        this.receiverName = receiverName
        this.senderProfileImg = senderProfileImg
        this.receiverProfileImg = receiverProfileImg

    }
}