package com.shan.mailtest.ui.main.draft

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.shan.mailtest.databinding.FragmentDraftBinding
import com.shan.mailtest.ui.main.Drawer
import com.shan.mailtest.ui.main.MainViewModel

class DraftFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var viewModel: DraftViewModel

    private lateinit var binding: FragmentDraftBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDraftBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DraftViewModel::class.java)
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