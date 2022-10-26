package com.hlayan.emailsample.ui.main.reply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hlayan.emailsample.databinding.FragmentReplyBinding
import com.hlayan.emailsample.ui.main.toast

class ReplyFragment : Fragment() {

    private lateinit var viewModel: ReplyViewModel
    private lateinit var binding: FragmentReplyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReplyViewModel::class.java)
        setUpToolbar()
        setOnSpinnerClickEvent()
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            title = null
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setOnSpinnerClickEvent() {
        binding.actionBarSpinner.setSelection(2, true)
        binding.actionBarSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    context?.toast("$parent\n $view\n $position\n $id")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }
}