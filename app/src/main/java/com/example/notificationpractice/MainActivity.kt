package com.example.notificationpractice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.notificationpractice.notification.basic.BasicNotification
import com.example.notificationpractice.ui.theme.NotificationPracticeTheme

class MainActivity : ComponentActivity() {

    private lateinit var myBasicNotification: BasicNotification

    private val requestPermissionsLuncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->

        if (isGranted) {
            Toast.makeText(this, "notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "notification permission not granted", Toast.LENGTH_SHORT).show()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        myBasicNotification = BasicNotification(this)

        setContent {
            NotificationPracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InitUi(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onClickButton = {code->

                            // android version is granter than 13
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                checkNotificationPermission(code)
                            } else {
                                showNotification(code)
                            }
                        }
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission(code: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
           showNotification(code)
        } else {
            requestPermissionsLuncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(code : Int){
        when(code){
            1-> {
                myBasicNotification.showNotification()
            }

            2-> {
                myBasicNotification.showNotificationWithButton()
            }

            3-> {
                myBasicNotification.showNotificationWithReply()
            }

        }
    }

}

@Composable
fun InitUi(modifier: Modifier, onClickButton: (Int) -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        OutlinedButton(
            onClick = {
                onClickButton.invoke(1)
            }
        ) {
            Text("show basic notification")
        }

        OutlinedButton(
            onClick = {
                onClickButton.invoke(2)
            }
        ) {
            Text("show notification with buttons")
        }

        OutlinedButton(
            onClick = {
                onClickButton.invoke(3)
            }
        ) {
            Text("show notification with reply field")
        }

    }
}


//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NotificationPracticeTheme {
//    }
//}

