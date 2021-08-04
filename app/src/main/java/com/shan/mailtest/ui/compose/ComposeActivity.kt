package com.shan.mailtest.ui.compose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.shan.mailtest.R
import com.shan.mailtest.databinding.ActivityComposeBinding
import com.shan.mailtest.mail.MailContent
import com.shan.mailtest.mail.MailSender
import com.shan.mailtest.mail.MailUser
import com.shan.mailtest.ui.main.isValidEmail
import com.shan.mailtest.ui.main.showInfoDialog
import com.shan.mailtest.ui.main.toast
import com.shan.mailtest.utils.UriParser

class ComposeActivity : AppCompatActivity() {

    lateinit var binding: ActivityComposeBinding

    private var composeAttachmentAdapter: ComposeAttachmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComposeBinding.inflate(layoutInflater).apply { setContentView(root) }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_compose, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_send -> sendComposedMail()
            R.id.menu_attachment -> pickFile()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            data?.data?.let { addAttachment(it) }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initView() {
        binding.tvMailAddressFrom.text = MailUser.EMAIL
        setComposeAttachmentAdapter()
    }

    private fun setComposeAttachmentAdapter() {
        composeAttachmentAdapter = ComposeAttachmentAdapter(layoutInflater)
        binding.rvAttachment.adapter = composeAttachmentAdapter
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 0)
    }

    private fun addAttachment(uri: Uri) {
        Log.d("myMessage", uri.toString())
        val uriParser = UriParser(this, uri)
        val fileName = uriParser.fileName
        val filePath = uriParser.filePath
        if (fileName != null && filePath != null) {
            Log.d("myMessage", filePath)
            val attachItem = AttachItem(fileName, filePath)
            composeAttachmentAdapter?.addNewItem(attachItem)
        } else {
            toast("Invalid attachment!")
        }
    }

    private fun sendComposedMail() {

        val to = binding.etTo.text.toString()
        val cc = binding.etCc.text.toString()
        val bcc = binding.etBcc.text.toString()
        val subject = binding.etSubject.text.toString()
        val message = binding.etMessage.text.toString()
        val attachment = composeAttachmentAdapter?.getItems() ?: listOf()

        if (to.isValidEmail()) {
            toast("Sending...")
            val mailContent = MailContent(subject, to, cc, bcc, message, attachment)
            MailSender.sendMail(this, mailContent)
        } else {
            showInfoDialog("The address <$to> is invalid.")
        }
    }
}