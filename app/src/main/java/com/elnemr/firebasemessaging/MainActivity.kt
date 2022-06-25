package com.elnemr.firebasemessaging

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.elnemr.firebasemessaging.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
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

        initSharedPreferences()
        getToken()
        subscribeToTopic(TOPIC)
        initClicks()
    }

    private fun initClicks() {
        binding.btnSend.setOnClickListener {
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

    private fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

    private fun initSharedPreferences() {
        FireBaseService.prefrences = getSharedPreferences("prefrences", MODE_PRIVATE)
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FireBaseService.token = it
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