package com.shan.mailtest.ui.main

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.shan.mailtest.R
import com.shan.mailtest.databinding.ActivityMainBinding
import com.shan.mailtest.ui.compose.ComposeActivity

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    @IdRes
    private var selectedMenuItem = R.id.nav_inbox //Default selected MenuItem
    private var isMenuItemClick = false

    private lateinit var inboxActionView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }

        navController = findNavController(R.id.nav_host_fragment)

        setPaddingOnNavHeaderLayout(binding.navView)

        inboxActionView = binding.navView.menu.findItem(R.id.nav_inbox).actionView as TextView

        setFabAction(binding.fab)
        fabExtendOrShrink()
        drawerOpenOrClose()
        onDestinationChanged()
        setNavigationItemSelectedListener()
        setDrawerListener()

    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }

    private fun setNavigationItemSelectedListener() {
        binding.navView.setCheckedItem(selectedMenuItem)
        inboxActionView.setCount(99, ContextCompat.getColor(this, R.color.primary))
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            isMenuItemClick = navController.currentDestination!!.id != menuItem.itemId
            selectedMenuItem = menuItem.itemId
            binding.drawerLayout.close()
            true
        }
    }

    private fun setDrawerListener() {
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
                if (isMenuItemClick) {
                    navController.navigate(selectedMenuItem, null, navOptions {
                        launchSingleTop = true
                        popUpTo = R.id.nav_inbox
                    })
                    isMenuItemClick = false
                    changeActionViewProperties()
                }
            }
        })
    }

    private fun changeActionViewProperties() {
        if (selectedMenuItem == R.id.nav_inbox) {
            inboxActionView.setCount(
                99,
                ContextCompat.getColor(baseContext, R.color.primary)
            )
        } else {
            inboxActionView.setCount(
                44,
                ContextCompat.getColor(baseContext, R.color.black)
            )
        }
    }

    private fun TextView.setCount(count: Int, @ColorInt color: Int) {
        setTextColor(color)
        gravity = Gravity.CENTER
        textSize = 12F
        setTypeface(null, Typeface.BOLD)
        text = count.toString()
    }

    private fun setFabAction(fab: ExtendedFloatingActionButton) {
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        fab.animation = animation
        fab.setOnClickListener {
            startActivity(Intent(this, ComposeActivity::class.java))
        }
    }

    private fun fabExtendOrShrink() {
        mainViewModel.onHideOrShow().observe(this) { fab ->
            when (fab) {
                Fab.SHOW -> {
                    binding.fab.extend()
                }
                Fab.HIDE -> {
                    binding.fab.shrink()
                }
                else -> {
                    /**
                     * I added else block because of
                     * "WHEN_ENUM_CAN_BE_NULL_IN_JAVA" warning
                     */
                }
            }
        }
    }

    private fun drawerOpenOrClose() {
        mainViewModel.onOpenOrClose().observe(this) { drawer ->
            when (drawer) {
                Drawer.OPEN -> {
                    binding.drawerLayout.open()
                }
                Drawer.CLOSE -> {
                    binding.drawerLayout.close()
                }
                else -> {
                    /**
                     * I added else block because of
                     * "WHEN_ENUM_CAN_BE_NULL_IN_JAVA" warning
                     */
                }
            }
        }
    }

    private fun setPaddingOnNavHeaderLayout(navigationView: NavigationView) {
        navigationView.getHeaderView(0).setPadding(0, getStatusBarHeight(), 0, 0)
    }

    private fun getStatusBarHeight(): Int {
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) resources.getDimensionPixelSize(resId) else 0
    }

    private fun onDestinationChanged() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_inbox,
                R.id.nav_outbox,
                R.id.nav_spam,
                R.id.nav_sent,
                R.id.nav_draft,
                R.id.nav_trash -> {

                    selectedMenuItem = destination.id

                    val menuItem = binding.navView.menu.findItem(selectedMenuItem)
                    if (!menuItem.isChecked) menuItem.isChecked = true
                    changeActionViewProperties()

                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.fab.apply {
                        show()
                        extend()
                    }
                }
                else -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    binding.fab.apply {
                        hide()
                        shrink()
                    }
                }
            }
        }
    }
}