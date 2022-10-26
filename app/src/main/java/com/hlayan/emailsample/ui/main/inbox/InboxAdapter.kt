package com.hlayan.emailsample.ui.main.inbox

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hlayan.emailsample.databinding.ItemInboxBinding
import com.hlayan.emailsample.ui.main.DEFAULT_DATE_PATTERN
import com.hlayan.emailsample.ui.main.getDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InboxAdapter(
    private val items: ArrayList<InboxModel>
) : RecyclerView.Adapter<InboxAdapter.ViewHolder>() {

    private var selectedItems = arrayListOf<Int>()
    private var selectionMode = false

    private val sparseBooleanArray = SparseBooleanArray()

    init {
        items.forEachIndexed { index, _ ->
            sparseBooleanArray.append(index, false)
        }
    }

    private lateinit var onClick: (InboxModel) -> Unit

    private lateinit var onSelect: (Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.count()

    fun getSelectedItemCount(): Int {
        return selectedItems.count()
    }

    fun getCurrentSparseCount(): Int {
        return sparseBooleanArray.size()
    }

    fun setOnItemClickListener(onClick: (InboxModel) -> Unit) {
        this.onClick = onClick
    }

    fun setOnItemSelectListener(onSelect: (Int) -> Unit) {
        this.onSelect = onSelect
    }

    fun onDeselectedAll() {
        if (selectedItems.isNotEmpty()) {
            selectedItems.forEach { index ->
                sparseBooleanArray.put(index, false)
            }
            selectedItems.clear()
            selectionMode = false
            notifyDataSetChanged()
        }

    }

    fun onDeleteSelectedItems() {
        if (selectionMode) {

            Collections.sort(selectedItems, Collections.reverseOrder())

            selectedItems.forEach { index ->
                items.removeAt(index)
                Log.d("index", index.toString())
            }
            selectedItems.clear()

            sparseBooleanArray.clear()
            items.forEachIndexed { index, _ ->
                sparseBooleanArray.append(index, false)
            }

            selectionMode = false
            notifyDataSetChanged()
        }
    }

    private fun onItemSelectionMode(position: Int) {
        val boolean = !sparseBooleanArray[position]
        sparseBooleanArray.put(position, boolean)
        if (boolean) {
            selectedItems.add(position)
        }
        if (!boolean) {
            selectedItems.remove(position)
        }
        if (selectedItems.isEmpty()) selectionMode = false
        onSelect(selectedItems.count())
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemInboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InboxModel, adapterPosition: Int) {
            binding.apply {

                root.isSelected = sparseBooleanArray[adapterPosition]

                val textColor = if (sparseBooleanArray[adapterPosition]) {
                    ContextCompat.getColor(root.context, android.R.color.white)
                } else {
                    ContextCompat.getColor(root.context, android.R.color.black)
                }

                textSenderId.setTextColor(textColor)
                textSubjectInbox.setTextColor(textColor)
                textReceivedTime.setTextColor(textColor)
                textMessagesInbox.setTextColor(textColor)

                val receivedTime = item.sentDate?.getDateFormat(DEFAULT_DATE_PATTERN) ?: "null"

                textSenderId.text = item.fromName
                textSubjectInbox.text = item.subject
                textReceivedTime.text = receivedTime
                textMessagesInbox.text = item.initMessage

                root.setOnClickListener {
                    if (selectionMode) {
                        onItemSelectionMode(adapterPosition)
                    } else {
                        onClick(item)
                    }
                }

                root.setOnLongClickListener {
                    if (selectionMode) {
                        onItemSelectionMode(adapterPosition)
                    } else {
                        selectionMode = true
                        selectedItems.add(adapterPosition)
                        sparseBooleanArray.put(adapterPosition, true)
                        onSelect(selectedItems.count())
                        notifyDataSetChanged()
                    }
                    true
                }
            }
        }
    }
}