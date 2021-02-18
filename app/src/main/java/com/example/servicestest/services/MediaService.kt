package com.example.servicestest.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.servicestest.R

class MediaService : Service() {
    //declare object MediaPlayer
    private lateinit var player: MediaPlayer
    private var CHANNEL_ID = "Stupid Channel"
    private var NOTIFICATION_ID = 10457
    private var userPrompted = false;

    private var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("NO Network")
            .setContentText("You're gonna need to connect to WiFi my dude.")
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)


        //  player.isLooping = true
        player.start()


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            startNetworkListen(connectivityManager)
        }
        //Returns Status of the Program
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startNetworkListen(connectivityManager: ConnectivityManager) {
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, object :
                ConnectivityManager.NetworkCallback() {

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    with(NotificationManagerCompat.from(this@MediaService)) {
                        // notificationId is a unique int for each notification that you must define
                        cancel(NOTIFICATION_ID)
                        userPrompted = false
                    }
                } else {
                    with(NotificationManagerCompat.from(this@MediaService)) {
                        // notificationId is a unique int for each notification that you must define
                        if (!userPrompted) {
                            notify(NOTIFICATION_ID, notification.build())
                            userPrompted = true;
                        }
                    }
                }
            }
            override fun onLost(network: Network) {
                super.onLost(network)
                with(NotificationManagerCompat.from(this@MediaService)) {
                    // notificationId is a unique int for each notification that you must define
                    if (!userPrompted) {
                        notify(NOTIFICATION_ID, notification.build())
                        userPrompted = true;
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        //Stopping process
        player.stop()

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Terry Town"
            val descriptionText = "This a town for Terry's"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}