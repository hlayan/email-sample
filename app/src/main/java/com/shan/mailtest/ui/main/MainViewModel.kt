package com.shan.mailtest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val hideOrShow = MutableLiveData<Fab>()

    fun setHideOrShow(value: Fab) {
        hideOrShow.value = value
    }

    fun onHideOrShow(): LiveData<Fab> = hideOrShow

    private val openOrClose = MutableLiveData<Drawer>()

    fun setOpenOrClose(value: Drawer) {
        openOrClose.value = value
    }

    fun onOpenOrClose(): LiveData<Drawer> = openOrClose

}