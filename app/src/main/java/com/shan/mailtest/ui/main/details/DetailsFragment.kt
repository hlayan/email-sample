package com.shan.mailtest.ui.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shan.mailtest.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onNavIconClick()

        binding.buttonGotoReply.isClickable = false
        binding.buttonGotoReply.setOnClickListener {
            goToReplyFragment()
        }

        binding.buttonGotoReplyAll.setOnClickListener {
            goToReplyFragment()
        }

        binding.buttonGotoForward.setOnClickListener {
            goToReplyFragment()
        }

    }

    private fun onNavIconClick() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun goToReplyFragment() {
        val action = DetailsFragmentDirections.actionNavDetailsToReplyFragment()
        findNavController().navigate(action)
    }
}