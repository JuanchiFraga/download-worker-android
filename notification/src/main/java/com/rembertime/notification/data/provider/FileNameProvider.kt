package com.rembertime.notification.data.provider

/**
 * This class tries to get the file name based on a path
 * input "http://www.domain.com/file.txt"
 * output "file.txt"
 */
internal class FileNameProvider {

    fun find(path: String): String {
        return path.substring(path.lastIndexOf("/").inc())
    }
}