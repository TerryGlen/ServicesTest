package com.example.servicestest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import com.example.servicestest.services.MediaService

class MainActivity : AppCompatActivity() , View.OnClickListener {
    private var start: Button? = null
    private var stop: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        //Declaring Objects of Button Class
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)

        start = findViewById(R.id.startButton)
        stop = findViewById(R.id.stopButton)

        start!!.setOnClickListener(this)
        stop!!.setOnClickListener(this)

    }
    override fun onClick(view: View){
        // process to be performed
        // if start button is clicked
        if (view === start) {

            // starting the service
            startService(Intent(this, MediaService::class.java))
        }

        // process to be performed
        // if stop button is clicked
        else if (view === stop) {

            // stopping the service
            stopService(Intent(this, MediaService::class.java))
        }
    }


}