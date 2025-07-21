package com.example.notificationpractice.notification.basic

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.notificationpractice.MainActivity
import com.example.notificationpractice.R
import com.example.notificationpractice.notification.MyBroadcastReceiver

class BasicNotification(private val context: Context) {

    private val basicNotificationId = 1
    private val notificationWithButtonId = 2
    private val notificationWithReplyId = 3

    private val notificationChannelId = "notification_channel_id"
    private val notificationChannelName = "notification_channel_name"

    init {
        buildNotificationChannel()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification() {
        val notification = buildNotification()

        NotificationManagerCompat.from(context).notify(basicNotificationId, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotificationWithButton() {
        val notification = buildNotificationWithButton()

        NotificationManagerCompat.from(context).notify(notificationWithButtonId, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotificationWithReply() {
        val notification = buildNotificationWithReply()

        NotificationManagerCompat.from(context).notify(notificationWithReplyId, notification)
    }


    private fun buildNotificationChannel() {
        val channel = NotificationChannel(
            notificationChannelId,
            notificationChannelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "basic notification description for overview"
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)

    }

    private fun buildNotification(): Notification {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val title = "basic notification"
        val contentBigText = "hello!, \n My name is Karanbir Singh. \n I'm recent BCA graduate"

        return NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.baseline_add_reaction_24)
            .setContentTitle(title)
            .setContentText(contentBigText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentBigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun buildNotificationWithButton(): Notification {
        val notificationTabIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notificationTabPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                notificationTabIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val addBtnIntent = Intent(context, MyBroadcastReceiver::class.java).apply {
            action = "ACTION_ADD"
        }

        val addBtnPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            addBtnIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val minusBtnIntent = Intent(context, MyBroadcastReceiver::class.java).apply {
            action = "ACTION_MINUS"
        }

        val minusBtnPendingIntent = PendingIntent.getBroadcast(
            context,
            -1,
            minusBtnIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.baseline_add_reaction_24)
            .setContentTitle("Notification with action buttons")
            .setContentText("Click Buttons")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(notificationTabPendingIntent)
            .addAction(R.drawable.baseline_add_reaction_24, "add", addBtnPendingIntent)
            .addAction(R.drawable.baseline_minus_circle_outline_24, "minus", minusBtnPendingIntent)
            .build()
    }

    private fun buildNotificationWithReply(): Notification {
        val replyTextKey = "reply_text_key"

        val remoteInput = RemoteInput.Builder(replyTextKey).run {
            setLabel("Enter you reply")
            setAllowFreeFormInput(true)
            build()
        }


        val replyIntent = Intent(context, MyBroadcastReceiver::class.java).apply {
            action = "ACTION_REPLY"
            putExtra("conversation_id", 1234)

        }

        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            1234,
            replyIntent,
             PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val replyAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.baseline_add_circle_outline_24,
            "Replay",
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()

        return NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.baseline_add_reaction_24)
            .setContentTitle("Notification with reply field")
            .setContentText("press reply button below")
            .addAction(replyAction)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
    }


}


