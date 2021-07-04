package com.rembertime.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rembertime.notification.work.DownloadFileNotificationWorker
import com.rembertime.notification.model.NotificationModel

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DownloadFileNotificationWorker.enqueue(this, NotificationModel(
            filePath = SAMPLE_BIN_1GB,
            applicationIcon = R.drawable.ic_bell,
            notificationIcon = R.drawable.ic_bell
        ))
    }

    companion object {
        const val SAMPLE_AVI = "https://filesamples.com/samples/video/avi/sample_1280x720_surfing_with_audio.avi"
        const val SAMPLE_VIDEO = "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_1920_18MG.mp4"
        const val SAMPLE_PDF = "https://s23.q4cdn.com/202968100/files/doc_downloads/test.pdf"
        const val SAMPLE_BIN_100MB = "https://speed.hetzner.de/100MB.bin"
        const val SAMPLE_BIN_1GB = "https://speed.hetzner.de/1GB.bin"
        const val SAMPLE_BIN_10GB = "https://speed.hetzner.de/10GB.bin"
    }
}