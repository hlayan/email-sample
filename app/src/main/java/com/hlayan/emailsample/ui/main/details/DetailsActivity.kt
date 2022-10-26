package com.hlayan.emailsample.ui.main.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hlayan.emailsample.databinding.ActivityDetailsBinding
import com.hlayan.emailsample.ui.main.DEFAULT_DATE_PATTERN
import com.hlayan.emailsample.ui.main.getDateFormat
import com.hlayan.emailsample.ui.main.inbox.InboxModel

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setOnClickListeners()
        inflateData()
    }

    private fun setOnClickListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun inflateData() {
        val inboxModel = intent.getParcelableExtra<InboxModel>("INBOX_MODEL")
        binding.apply {
            inboxModel?.let {
                val receivedTime = it.sentDate?.getDateFormat(DEFAULT_DATE_PATTERN) ?: "null"

                tvSubject.text = it.subject
                tvDate.text = receivedTime
                tvFrom.text = it.fromAddress
                tvTo.text = it.to

                wvContent.loadDataWithBaseURL(
                    null,
                    it.messages,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        }
    }
}