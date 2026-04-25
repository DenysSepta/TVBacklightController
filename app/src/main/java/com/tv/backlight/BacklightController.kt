package com.tv.backlight

import android.content.Context
import android.provider.Settings
import android.util.Log

object BacklightController {

    var value = 50
        private set

    fun increase(context: Context) {
        value = (value + 5).coerceAtMost(100)
        apply(context)
    }

    fun decrease(context: Context) {
        value = (value - 5).coerceAtLeast(0)
        apply(context)
    }

    private fun apply(context: Context) {
        try {
            Log.d("TVBACKLIGHT", "Setting global picture_backlight to: $value")
            val success = Settings.Global.putInt(context.contentResolver, "picture_backlight", value)
            if (!success) {
                Log.w("TVBACKLIGHT", "Failed to set value via Settings.Global")
            }
        } catch (e: Exception) {
            Log.e("TVBACKLIGHT", "Permission denied or error", e)
            Log.d("TVBACKLIGHT", "Try granting permission: adb shell pm grant ${context.packageName} android.permission.WRITE_SECURE_SETTINGS")
        }
    }
}
