
package com.nothing.musicvisualizer

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity

class MainActivity : Activity() {

    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.rgb(25, 19, 22))
        }

        val title = TextView(this).apply {
            text = "NOTHING  MUSIC"
            textSize = 26f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        status = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 32, 0, 32)
        }

        val button = Button(this).apply {
            text = "РАЗРЕШИТЬ ДОСТУП К МУЗЫКЕ"
            setOnClickListener {
                startActivity(
                    Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                )
            }
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(button)

        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()

        if (!::status.isInitialized) return

        val enabled = android.provider.Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )?.contains(packageName) == true

        status.text = if (enabled) {
            "● ДОСТУП РАЗРЕШЁН"
        } else {
            "○ ДОСТУП НЕ ВКЛЮЧЁН"
        }
    }
}
