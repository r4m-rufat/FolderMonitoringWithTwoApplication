package com.codingwithrufat.backgroundservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.FileObserver
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

class CheckFolderService : Service() {

    companion object {

        var fileObserver: FileObserver? = null

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AppDebug", "onStartCommand: Service is running")
        fileObserver = object : FileObserver("$ROOT_DIR/Printer", CREATE) {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onEvent(event: Int, file: String?) {

                file?.let {
                    val createdFile = File("${ROOT_DIR}/Printer", file)

                    // create folder for new file
                    val folder_path = File(ROOT_DIR, "PrinterDeletedFile")
                    if (!folder_path!!.exists()) {
                        folder_path!!.mkdirs()
                    }

                    CoroutineScope(IO).launch {

                        delay(2000L) // wait file creating in directory completely

                        /**
                         * we copy file from one folder to another
                         */
                        Files.copy(
                            Path("${ROOT_DIR}/Printer/${file}"),
                            Path("${ROOT_DIR}/PrinterDeletedFile/${file}")
                        )

                        delay(2000L) // after 2000ms we sure that file is copied another directory

                        createdFile.delete() // old file is deleted

                    }

                }

            }
        }

        fileObserver?.startWatching() // to observe folder while service is running


        val CHANNEL_ID = "Foreground"
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        )

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentText("Folder is monitoring")
            .setContentTitle("Service")
            .setSmallIcon(R.drawable.ic_launcher_background)

        startForeground(100, notification.build()) // service is working background

        return super.onStartCommand(intent, flags, startId)
    }
}