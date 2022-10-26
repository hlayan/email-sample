package com.hlayan.emailsample.ui.main.sent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider

import com.hlayan.emailsample.databinding.FragmentSentBinding
import com.hlayan.emailsample.ui.main.Drawer
import com.hlayan.emailsample.ui.main.MainViewModel

class SentFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var sentViewModel: SentViewModel

    private lateinit var binding: FragmentSentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpToolbar()
        sentViewModel = ViewModelProvider(this)[SentViewModel::class.java]
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                mainViewModel.setOpenOrClose(Drawer.OPEN)
            }
        }
    }
}