package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private val NOTIFICATION_ID = 0
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            var url : String? = null
            when (radio_group.checkedRadioButtonId) {
                -1 -> Toast.makeText(this, getString(R.string.please_select), Toast.LENGTH_LONG).show()
                R.id.radio_glide_button -> {
                    url = getString(R.string.radio_glide_url)
                    fileName = getString(R.string.radio_glide_text)
                }
                R.id.radio_udacity_button -> {
                    url = getString(R.string.radio_udacity_url)
                    fileName = getString(R.string.radio_udacity_text)
                }
                R.id.radio_retrofit_button -> {
                    url = getString(R.string.radio_retrofit_url)
                    fileName = getString(R.string.radio_retrofit_text)
                }
            }
            if (null != url) {
                download(url)
            }
            custom_button.buttonState = ButtonState.Loading

            // initialize the notification manager
            notificationManager = ContextCompat.getSystemService(
                    applicationContext, NotificationManager::class.java) as NotificationManager

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            status = getString(R.string.success)

            custom_button.buttonState = ButtonState.Completed

            val contentIntent = Intent(applicationContext, DetailActivity::class.java)
            contentIntent.putExtra(Companion.FILE_NAME, Companion.fileName)
            contentIntent.putExtra(Companion.STATUS, status)
            pendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    NOTIFICATION_ID,
                    contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            notificationManager.sendNotification(
                    getString(R.string.notification_title),
                    getString(R.string.notification_description),
                    applicationContext,
                    pendingIntent,
                    NOTIFICATION_ID
            )
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        const val FILE_NAME = "FILE_NAME"
        const val STATUS = "STATUS"

        private lateinit var fileName : String
        private lateinit var status : String
    }

}

