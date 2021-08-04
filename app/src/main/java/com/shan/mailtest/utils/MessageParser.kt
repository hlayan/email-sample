package com.shan.mailtest.utils

import java.io.IOException
import javax.mail.BodyPart
import javax.mail.Message
import javax.mail.Message.RecipientType
import javax.mail.Message.RecipientType.*
import javax.mail.MessagingException
import javax.mail.internet.ContentType
import javax.mail.internet.MimeMultipart
import javax.mail.internet.MimeUtility

class MessageParser(private val message: Message) {

    val toAddresses = TO.text

    val ccAddresses = CC.text

    val bccAddresses = BCC.text

    val fromName get(): String = fromAddress.substring(0, fromAddress.indexOf("<") - 1)

    val fromAddress: String = MimeUtility.decodeText(message.from[0].toString())

    fun getInitMessage(): String {
        val string = message.content.toString()
        return if (string.count() > 10) {
            string.substring(0, 10)
        } else {
            string
        }
    }

    fun getBody(): String {
        return when {
            message.isMimeType("text/plain") -> message.content.toString()
            message.isMimeType("multipart/*") -> {
                val mimeMultipart = message.content as MimeMultipart
                mimeMultipart.getTextFromMimeMultipart()
            }
            message.isMimeType("text/html") -> message.content.toString()
            else -> ""
        }
    }

    @Throws(IOException::class, MessagingException::class)
    private fun MimeMultipart.getTextFromMimeMultipart(): String {
        if (count == 0) throw MessagingException("Multipart with no body parts not supported.")
        val isMultipartRelated = ContentType(contentType).match("multipart/related")
        if (isMultipartRelated) {
            val part = getBodyPart(0)
            val isMultipartAlt = ContentType(part.contentType).match("multipart/alternative")
            if (isMultipartAlt) {
                val mimeMultipart = part.content as MimeMultipart
                return mimeMultipart.getTextFromMimeMultipart()
            }
        } else {
            val isMultipartAlt = ContentType(contentType).match("multipart/alternative")
            if (isMultipartAlt) {
                for (i in 0 until count) {
                    val part = getBodyPart(i)
                    if (part.isMimeType("text/html")) {
                        return part.getTextFromBodyPart()
                    }
                }
            }
        }
        var result = ""
        for (i in 0 until count) {
            val bodyPart = getBodyPart(i)
            result += bodyPart.getTextFromBodyPart()
        }
        return result
    }

    @Throws(IOException::class, MessagingException::class)
    private fun BodyPart.getTextFromBodyPart(): String {
        return when {
            isMimeType("text/plain") -> content as String
            isMimeType("text/html") -> content as String
            content is MimeMultipart -> {
                val mimeMultipart = content as MimeMultipart
                mimeMultipart.getTextFromMimeMultipart()
            }
            else -> ""
        }
    }

    private val RecipientType.text
        get(): String {
            var listAddress = ""
            val address = message.getRecipients(this)
            if (address != null) {
                for (i in address.indices) {
                    listAddress += address[i].toString() + ", "
                }
            }
            if (listAddress.length > 1) {
                listAddress = listAddress.substring(0, listAddress.length - 2)
            }
            return listAddress
        }

}