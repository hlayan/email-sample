package com.shan.mailtest.ui.main.inbox

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shan.mailtest.mail.MailChecker
import com.shan.mailtest.utils.MessageParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class InboxViewModel : ViewModel() {

    private val _inboxList = MutableLiveData<ArrayList<InboxModel>>()

    fun setInboxList() = viewModelScope.launch {

        val inboxModelList = arrayListOf<InboxModel>()

        try {

            val messageArrayList = withContext(Dispatchers.Default) {
                MailChecker.checkMail()
            }

            messageArrayList.forEach {

                val msgParser = MessageParser(it)

                val messageNumber = it.messageNumber
                val fromName = msgParser.fromName
                val fromAddress = msgParser.fromAddress
                val subject = it.subject
                val to = msgParser.toAddresses
                val cc = msgParser.ccAddresses
                val bcc = msgParser.bccAddresses
                val sentDate: Date? = it.sentDate
                var messages: String
                var initMessage: String
                withContext(Dispatchers.Default) {
                    messages = msgParser.getBody()
                    initMessage = msgParser.getInitMessage()
                }

                val inbox = InboxModel(
                    messageNumber,
                    subject, fromName,
                    fromAddress,
                    to, cc, bcc,
                    sentDate, messages,
                    initMessage
                )

                inboxModelList.add(inbox)
            }

        } catch (e: Exception) {
            Log.e("exception_view_model", e.toString())
        }

        _inboxList.value = inboxModelList
    }

    fun getInboxList(): LiveData<ArrayList<InboxModel>> {
        return _inboxList
    }
}