package com.shan.mailtest.ui.main.inbox

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class InboxModel(
    val messageNumber: Int,
    val subject: String,
    val fromName: String,
    val fromAddress: String,
    val to: String,
    val cc: String,
    val bcc: String,
    val sentDate: Date?,
    val messages: String,
    val initMessage: String
) : Parcelable