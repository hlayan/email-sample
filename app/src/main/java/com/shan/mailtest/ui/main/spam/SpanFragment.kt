package com.shan.mailtest.ui.main.spam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.shan.mailtest.databinding.FragmentSpamBinding
import com.shan.mailtest.ui.main.Drawer
import com.shan.mailtest.ui.main.MainViewModel

class SpanFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var viewModel: SpanViewModel

    private lateinit var binding: FragmentSpamBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpamBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SpanViewModel::class.java)
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                mainViewModel.setOpenOrClose(Drawer.OPEN)
            }
        }
    }

}