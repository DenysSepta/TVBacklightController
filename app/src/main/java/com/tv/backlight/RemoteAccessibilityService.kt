package com.tv.backlight

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ProgressBar
import android.widget.TextView

class RemoteAccessibilityService : AccessibilityService() {

    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null
    private val handler = Handler(Looper.getMainLooper())
    private val hideRunnable = Runnable { hideOverlay() }

    private var repeatingKey: Int? = null
    private val repeatRunnable = object : Runnable {
        override fun run() {
            repeatingKey?.let {
                handleKey(it)
                handler.postDelayed(this, 100)
            }
        }
    }


    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    Log.d("TVBACKLIGHT", "Screen OFF - System goes to sleep")
                    // 1. Hide the overlay and stop any repeating key action.
                    hideOverlay()
                    repeatingKey = null
                    handler.removeCallbacksAndMessages(null)

                    // 2. Stop the main foreground service.
                    val serviceIntent = Intent(context, BacklightService::class.java)
                    context.stopService(serviceIntent)
                }
                Intent.ACTION_SCREEN_ON -> {
                    Log.d("TVBACKLIGHT", "Screen ON - System wakes up")
                    // Restart the backlight service when the screen turns on.
                    val serviceIntent = Intent(context, BacklightService::class.java)
                    // Android 8.0 and later require startForegroundService for this service.
                    context.startForegroundService(serviceIntent)
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        Log.d("TVBACKLIGHT", "Accessibility connected")

        // Register the screen-state receiver when the Accessibility service connects.
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        registerReceiver(screenStateReceiver, filter)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onKeyEvent(event: KeyEvent): Boolean {
        Log.d("TVBACKLIGHT", "Key code: ${event.keyCode} action:${event.action}")

        if (event.keyCode == KeyEvent.KEYCODE_CHANNEL_UP || event.keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (repeatingKey == null) {
                    repeatingKey = event.keyCode
                    handleKey(event.keyCode)
                    handler.postDelayed(repeatRunnable, 500) // Initial delay before repeat
                }
            } else if (event.action == KeyEvent.ACTION_UP) {
                if (repeatingKey == event.keyCode) {
                    repeatingKey = null
                    handler.removeCallbacks(repeatRunnable)
                }
            }
            return true
        }

        return false
    }

    private fun handleKey(keyCode: Int) {
        when (keyCode) {
            KeyEvent.KEYCODE_CHANNEL_UP -> BacklightController.increase(this)
            KeyEvent.KEYCODE_CHANNEL_DOWN -> BacklightController.decrease(this)
        }
        showOverlay()
    }

    private fun showOverlay() {
        handler.removeCallbacks(hideRunnable)

        if (overlayView == null) {
            val inflater = LayoutInflater.from(this)
            overlayView = inflater.inflate(R.layout.backlight_overlay, null)

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                y = 100
            }
            windowManager.addView(overlayView, params)
        }

        overlayView?.let {
            val progressBar = it.findViewById<ProgressBar>(R.id.backlight_progress)
            val label = it.findViewById<TextView>(R.id.backlight_label)
            val currentVal = BacklightController.value
            progressBar.progress = currentVal
            label.text = "Backlight: $currentVal%"
        }

        handler.postDelayed(hideRunnable, 3000)
    }

    private fun hideOverlay() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hideOverlay()
        handler.removeCallbacksAndMessages(null)

        try {
            unregisterReceiver(screenStateReceiver)
        } catch (e: Exception) {
            Log.e("TVBACKLIGHT", "Receiver not registered", e)
        }
    }
}