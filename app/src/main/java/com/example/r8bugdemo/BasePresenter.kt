package com.example.r8bugdemo

import android.util.Log

abstract class BasePresenter {

    fun onCrashButtonClick(unusedParameter: String?) {
        Log.d("BasePresenter", "Crash!")
    }
}
