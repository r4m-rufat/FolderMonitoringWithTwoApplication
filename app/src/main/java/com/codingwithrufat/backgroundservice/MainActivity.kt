package com.codingwithrufat.backgroundservice

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.nio.file.Files
import kotlin.io.path.Path

class MainActivity : AppCompatActivity() {

    val br = Reciver()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filter = IntentFilter(Intent.ACTION_BOOT_COMPLETED).apply {
            "com.codingwithrufat.backgroundservice.PrinterService"
        }
        registerReceiver(br, filter)
        this.startForegroundService(Intent(this, CheckFolderService::class.java))

    }

}