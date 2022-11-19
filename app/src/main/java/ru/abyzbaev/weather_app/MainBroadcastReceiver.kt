package ru.abyzbaev.weather_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.lang.StringBuilder

class MainBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("СИСТЕМНОЕ СООБЩЕНИЕ\n")
            append("Action: ${intent.action}")
            toString().also {
                Toast.makeText(context,it, Toast.LENGTH_LONG).show()
            }
        }
    }

}