package com.shan.mailtest.ui.main.outbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.shan.mailtest.databinding.FragmentOutboxBinding
import com.shan.mailtest.ui.main.Drawer
import com.shan.mailtest.ui.main.MainViewModel

class OutboxFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var viewModel: OutboxViewModel

    private lateinit var binding: FragmentOutboxBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOutboxBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OutboxViewModel::class.java)
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