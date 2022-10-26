package com.hlayan.emailsample.mail

data class MailContent(
    val subject: String,
    val to: String,
    val cc: String,
    val bcc: String,
    val message: String,
    val attachment: List<String>
)
