package com.example.notificationpractice.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.RemoteInput
import com.example.notificationpractice.notification.basic.BasicNotification

class MyBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("BroadcastReceiver", "onReceive triggered")

        when(intent?.action){

            "ACTION_ADD" ->{
                Toast.makeText(context, "plus button", Toast.LENGTH_LONG).show()
            }
            "ACTION_MINUS" ->{
                Toast.makeText(context, "minus button", Toast.LENGTH_LONG).show()
            }

            "ACTION_REPLY" ->{

                val replyTextKey = "reply_text_key"
                val userReplied = RemoteInput.getResultsFromIntent(intent)?.getString(replyTextKey) ?: "null"

                Log.d("BroadcastReceiver", "onReceive triggered replied text is - $userReplied")
                Toast.makeText(context, "User replied - $userReplied", Toast.LENGTH_LONG).show()
            }

        }
    }

}