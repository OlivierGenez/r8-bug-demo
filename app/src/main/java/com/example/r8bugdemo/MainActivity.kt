package com.example.r8bugdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.crashButton

class MainActivity : AppCompatActivity() {

    private val presenter: MainPresenterContract = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        crashButton.setOnClickListener {
            presenter.onCrashButtonClick("Crash!")
        }
    }
}
