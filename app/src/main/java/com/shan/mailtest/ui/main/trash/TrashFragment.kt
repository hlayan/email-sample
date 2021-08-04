package com.shan.mailtest.ui.main.trash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider

import com.shan.mailtest.databinding.FragmentTrashBinding
import com.shan.mailtest.ui.main.Drawer
import com.shan.mailtest.ui.main.MainViewModel

class TrashFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var trashViewModel: TrashViewModel

    private lateinit var binding: FragmentTrashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpToolbar()

        trashViewModel = ViewModelProvider(this)[TrashViewModel::class.java]
        trashViewModel.text.observe(viewLifecycleOwner, {
            binding.textSlideshow.text = it
        })
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                mainViewModel.setOpenOrClose(Drawer.OPEN)
            }
        }
    }
}