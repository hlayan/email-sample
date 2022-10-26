package com.hlayan.emailsample.ui.compose

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hlayan.emailsample.databinding.ItemAttachmentBinding

class ComposeAttachmentAdapter(
    private val layoutInflater: LayoutInflater,
    private val items: ArrayList<AttachItem> = arrayListOf()
) : RecyclerView.Adapter<ComposeAttachmentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttachmentBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.binding.apply {
            attachmentName.text = item.fileName
            close.setOnClickListener {
                items.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    fun addNewItem(item: AttachItem) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun getItems(): ArrayList<String> {
        val filePathList = arrayListOf<String>()
        items.forEach { filePathList.add(it.filePath) }
        return filePathList
    }
}