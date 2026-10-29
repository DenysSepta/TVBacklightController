package com.tv.backlight

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The launcher activity intentionally closes immediately.
        finish()
    }
}