package com.elnemr.firebasemessaging

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.elnemr.firebasemessaging.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FireBaseService.prefrences = getSharedPreferences("prefrences", MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d(TAG, "onCreate: $it")
            FireBaseService.token = it
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.btnSend.setOnClickListener {
            Log.d(TAG, "onCreate: here")
            val notificationData = NotificationData(
                binding.etTitle.text.toString(),
                binding.etMessage.text.toString()
            )
            PushNotification(
                notificationData,
                TOPIC
            // or send the notification to a firebase token instead of the TOPIC
            ).also {
                sendNotificationRequest(it)
            }

        }

    }

    private fun sendNotificationRequest(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d(TAG, "Success")
                } else {
                    Log.d(TAG, "Failed: ${response.errorBody().toString()}")
                }
            } catch (e: HttpException) {
                Log.d(TAG, "HttpException: $e")
            } catch (e: Exception) {
                Log.d(TAG, "Exception: $e")
            }
        }
}