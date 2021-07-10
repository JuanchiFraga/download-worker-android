package com.rembertime.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.rembertime.app.databinding.ActivityMainBinding
import com.rembertime.notification.work.DownloadFileNotificationWorker
import com.rembertime.notification.model.NotificationModel

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindButton(binding.failFileButton, SAMPLE_FAIL)
        bindButton(binding.mp4Button, SAMPLE_MP4, "video.mp4")
        bindButton(binding.pdfButton, SAMPLE_PDF, "book.pdf")
        bindButton(binding.binHundredMbButton, SAMPLE_BIN_100MB)
        bindButton(binding.binOneGbButton, SAMPLE_BIN_1GB)
        bindButton(binding.binTenGbButton, SAMPLE_BIN_10GB)
    }

    private fun bindButton(button: MaterialButton, filePath: String, customName: String? = null) = button.setOnClickListener {
        DownloadFileNotificationWorker.enqueue(this, NotificationModel(
            filePath = filePath,
            customFileName = customName,
            applicationIcon = R.drawable.ic_bell,
            notificationIcon = R.drawable.ic_folder
        ))
    }

    companion object {
        const val SAMPLE_FAIL = "https://domain.com/fail.pdf"
        const val SAMPLE_MP4 = "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_1920_18MG.mp4"
        const val SAMPLE_PDF = "https://research.nhm.org/pdfs/29985/29985-001.pdf"
        const val SAMPLE_BIN_100MB = "https://speed.hetzner.de/100MB.bin"
        const val SAMPLE_BIN_1GB = "https://speed.hetzner.de/1GB.bin"
        const val SAMPLE_BIN_10GB = "https://speed.hetzner.de/10GB.bin"
    }
}