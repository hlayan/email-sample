package com.hlayan.emailsample.ui.main.inbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hlayan.emailsample.R
import com.hlayan.emailsample.databinding.DialogDeleteItemBinding
import com.hlayan.emailsample.databinding.FragmentInboxBinding
import com.hlayan.emailsample.ui.main.*
import com.hlayan.emailsample.ui.main.details.DetailsActivity

class InboxFragment : Fragment() {

    private lateinit var inboxAdapter: InboxAdapter
    private lateinit var inboxViewModel: InboxViewModel
    private lateinit var binding: FragmentInboxBinding

    private var navIcon = NavIcon.HAMBURGER
    private var shouldInflateMenu = 1 // 1 is to inflate menu and 0 to not

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInboxBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        inboxViewModel = ViewModelProvider(this)[InboxViewModel::class.java]
        inboxViewModel.getInboxList().observe(viewLifecycleOwner) { inboxList ->
            binding.recyclerviewInbox.apply {

                val decorator =
                    DividerItemDecorationLastExcluded(R.color.divider)
                addItemDecoration(decorator)

                layoutManager = LinearLayoutManager(context)
                adapter = InboxAdapter(inboxList).apply {

                    inboxAdapter = this

                    setOnItemClickListener {
//                        val directions = InboxFragmentDirections.actionNavInboxToNavDetails()
//                        findNavController().navigateSafe(directions)

                        startActivity(Intent(requireContext(), DetailsActivity::class.java)
                            .apply {
                                showLog(it.initMessage)
                                putExtra("INBOX_MODEL", it)
                            }
                        )
                    }

                    setOnItemSelectListener { selectedItemsCount ->

                        when (selectedItemsCount) {
                            0 -> {
                                onNavCloseClick(binding.root.context)
                            }
                            else -> {
                                onMenuItemSelection(binding.root.context, selectedItemsCount)
                            }
                        }
                    }
                }
            }
        }

        inboxViewModel.setInboxList()

        setOnRecyclerScrollListener()
        setOnMenuItemClickListener()
        setOnNavIconsClickListener(binding.root.context)
        setOnBackPressedListener(binding.root.context)
    }

    private fun setOnBackPressedListener(context: Context) {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (navIcon) {
                    NavIcon.HAMBURGER -> {
                        if (isEnabled) {
                            isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }
                    NavIcon.CLOSE -> {
                        onNavCloseClick(context)
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    private fun setOnRecyclerScrollListener() {
        binding.recyclerviewInbox.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    // On Scroll Down
                    mainViewModel.setHideOrShow(Fab.HIDE)
                } else if (dy < 0) {
                    // On Scroll Up
                    mainViewModel.setHideOrShow(Fab.SHOW)
                }
            }
        })
    }

    private fun setOnNavIconsClickListener(context: Context) {
        binding.toolbar.setNavigationOnClickListener {
            when (navIcon) {
                NavIcon.HAMBURGER -> {
                    mainViewModel.setOpenOrClose(Drawer.OPEN)
                }
                NavIcon.CLOSE -> {
                    onNavCloseClick(context)
                }
            }
        }
    }

    private fun onNavCloseClick(context: Context) {
        changeMenu(R.menu.menu_main)
        navIcon = NavIcon.HAMBURGER
        binding.toolbar.title = getString(R.string.menu_inbox)
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(
            context,
            R.drawable.ic_navigate_hamburger
        )
        inboxAdapter.onDeselectedAll()
        shouldInflateMenu = 1
    }

    private fun changeMenu(@MenuRes menuRes: Int) {
        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(menuRes)
    }

    private fun setOnMenuItemClickListener() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_archive -> {
                    context?.toast(inboxAdapter.getSelectedItemCount().toString())
                }
                R.id.menu_delete -> {
                    setOnDeleteMenuItemClick(
                        binding.root.context,
                        inboxAdapter.getSelectedItemCount()
                    )
                }
                R.id.menu_markunread -> {
                    context?.toast(inboxAdapter.getCurrentSparseCount().toString())
                }
                R.id.menu_search -> {
                    context?.toast("Setting")
                }
            }
            true
        }
    }

    private fun setOnDeleteMenuItemClick(context: Context, selectedItemCount: Int) {
        val layoutInflater = LayoutInflater.from(context)
        val dialogDeleteItemBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialogDeleteItemBinding.showCustomDialog(
            resources,
            requireActivity().windowManager
        ) { dialog, deleteItemBinding ->

            val title = "Delete $selectedItemCount chats"
            deleteItemBinding.textTitleDialog.text = title
            deleteItemBinding.textDescriptionDialog.text =
                getString(R.string.dialog_description_text)
            deleteItemBinding.textButtonNegativeDialog.setOnClickListener {
                dialog.dismiss()
            }
            deleteItemBinding.textButtonPositionDialog.setOnClickListener {
                shouldInflateMenu = 1
                inboxAdapter.onDeleteSelectedItems()
                navIcon = NavIcon.HAMBURGER
                binding.toolbar.title = getString(R.string.menu_inbox)
                binding.toolbar.navigationIcon = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_navigate_hamburger
                )
                changeMenu(R.menu.menu_main)
                dialog.dismiss()
            }
        }
    }

    private fun onMenuItemSelection(context: Context, selectedItemsCount: Int) {

        binding.toolbar.title = selectedItemsCount.toString()

        if (selectedItemsCount == shouldInflateMenu) {
            navIcon = NavIcon.CLOSE
            changeMenu(R.menu.menu_details)
            binding.toolbar.navigationIcon = ContextCompat.getDrawable(
                context,
                R.drawable.ic_navigate_close
            )
            shouldInflateMenu = 0
        }
    }
}