package com.hlayan.emailsample.mail

import android.content.Context
import android.util.Log
import com.hlayan.emailsample.mail.MailUser.EMAIL
import com.hlayan.emailsample.mail.MailUser.MESSAGE_TYPE
import com.hlayan.emailsample.mail.MailUser.PASSWORD
import com.hlayan.emailsample.mail.MailUser.SMTP_HOST
import com.hlayan.emailsample.mail.MailUser.SMTP_PORT
import com.hlayan.emailsample.ui.main.isValidEmail
import com.hlayan.emailsample.ui.main.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * https://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
 *
 * https://chetan-garg36.medium.com/android-send-mails-not-intent-642d2a71d2ee
 */

object MailSender {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    private fun getProperties(): Properties {
        return Properties().also {
            it["mail.smtp.host"] = SMTP_HOST
            it["mail.smtp.port"] = SMTP_PORT
            it["mail.smtp.auth"] = "true"
        }
    }

    private fun getSession(): Session {
        return Session.getInstance(getProperties(), object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EMAIL, PASSWORD)
            }
        })
    }

    private fun getMimeMessage(mailContent: MailContent): MimeMessage {

        return MimeMessage(getSession()).also { mime ->

            mime.subject = mailContent.subject
            mime.setFrom(InternetAddress(EMAIL, "Name"))
            mime.addRecipient(Message.RecipientType.TO, InternetAddress(mailContent.to))

            if (mailContent.cc.isValidEmail()) {
                mime.addRecipient(Message.RecipientType.CC, InternetAddress(mailContent.cc))
            }

            if (mailContent.bcc.isValidEmail()) {
                mime.addRecipient(Message.RecipientType.BCC, InternetAddress(mailContent.bcc))
            }

            val multipart = getMultipart(mailContent.message, mailContent.attachment)
            mime.setContent(multipart)

        }
    }

    private fun getMultipart(
        message: String,
        attachments: List<String>
    ): Multipart {

        //Message
        val bodyPartMessage = MimeBodyPart()
        bodyPartMessage.setContent(message, MESSAGE_TYPE)

        //Attachment
        val bodyPartAttachment = MimeBodyPart()
        attachments.forEach { file ->
            bodyPartAttachment.attachFile(file)
        }

        return MimeMultipart().apply {
            addBodyPart(bodyPartMessage)
            if (attachments.isNotEmpty()) {
                addBodyPart(bodyPartAttachment)
            }
        }
    }

    fun sendMail(
        context: Context,
        mailContent: MailContent
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            try {
                val mimeMessage = getMimeMessage(mailContent)

                Transport.send(mimeMessage)

                withContext(Dispatchers.Main) {
                    context.toast("Sending Successful!")
                }

            } catch (e: Exception) {
                Log.e("exception", e.toString())
                withContext(Dispatchers.Main) {
                    context.toast("Sending Failed!")
                }
            }
        }
    }
}