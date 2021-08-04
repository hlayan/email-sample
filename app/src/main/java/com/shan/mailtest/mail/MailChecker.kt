package com.shan.mailtest.mail

import android.util.Log
import com.shan.mailtest.mail.MailUser.EMAIL
import com.shan.mailtest.mail.MailUser.PASSWORD
import com.shan.mailtest.mail.MailUser.POP3_HOST
import com.shan.mailtest.mail.MailUser.POP3_PORT
import java.util.*
import javax.mail.*

/**
 * https://blog.eduonix.com/java-programming-2/learn-use-java-mail-api-send-receive-emails/#
 */
object MailChecker {

    private fun getProperties(): Properties {
        return Properties().also {
            it["mail.pop3.host"] = POP3_HOST
            it["mail.pop3.port"] = POP3_PORT
        }
    }

    private fun getSession(): Session {
        return Session.getInstance(getProperties(), object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EMAIL, PASSWORD)
            }
        })
    }

    private fun getFolder(): Folder? {

        //Create POP3 store object and connect with the server
        val store = getSession().getStore("pop3")
        store.connect(POP3_HOST, EMAIL, PASSWORD)

        //Create folder object and open it in read-only mode
        val folder = store.getFolder("INBOX")
        folder.open(Folder.READ_ONLY)

        return folder
    }

    fun checkMail(): ArrayList<Message> {

        val messageArray: ArrayList<Message> = arrayListOf()

        try {

            getFolder()?.messages?.forEach {

                val subject = "Subject: ${it.subject}"
                Log.d("message", subject)

                messageArray.add(it)
            }

        } catch (exp: Exception) {
            Log.e("exception_mail", exp.toString())
        }

        return messageArray
    }
}