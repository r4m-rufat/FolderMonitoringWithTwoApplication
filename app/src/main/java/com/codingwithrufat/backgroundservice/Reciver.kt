package com.codingwithrufat.backgroundservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

class Reciver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AppDebug", "onReceive: Broadcast is working ")

        // when app is reboot condition then BroadcastReceiver wakes up services
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action){
            context?.startForegroundService(Intent(context, CheckFolderService::class.java))
        }

        /**
         * custom intent action which is sent from printer application
         */
        if ("com.codingwithrufat.backgroundservice.PrinterService" == intent?.action){
            context?.startForegroundService(Intent(context, CheckFolderService::class.java))
        }

    }
}