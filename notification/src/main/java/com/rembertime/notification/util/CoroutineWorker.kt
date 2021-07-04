package com.rembertime.notification.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.ForegroundInfo
import androidx.work.Data
import androidx.work.await
import androidx.work.impl.utils.futures.SettableFuture
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import java.lang.Runnable

/**
 * A [ListenableWorker] implementation that provides interop with Kotlin Coroutines.  Override
 * the [doWork] function to do your suspending work.
 * <p>
 * By default, CoroutineWorker runs on [Dispatchers.Default]; this can be modified by
 * overriding [coroutineContext].
 * <p>
 * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
 * [ListenableWorker.Result].  After this time has expired, the worker will be signalled to stop.
 */
@SuppressLint("RestrictedApi")
abstract class CoroutineWorker(
    private val appContext: Context,
    params: WorkerParameters
) : ListenableWorker(appContext, params) {

    private val job = Job()
    private val future: SettableFuture<Result> = SettableFuture.create()

    init {
        future.addListener(
            Runnable {
                if (future.isCancelled) {
                    job.cancel()
                }
            },
            taskExecutor.backgroundExecutor
        )
    }

    /**
     * The coroutine context on which [doWork] will run. By default, this is [Dispatchers.Default].
     */
    @Deprecated(message = "use withContext(...) inside doWork() instead.")
    open val coroutineContext: CoroutineDispatcher = Dispatchers.Default

    @Suppress("DEPRECATION")
    final override fun startWork(): ListenableFuture<Result> {

        val coroutineScope = CoroutineScope(coroutineContext + job)
        coroutineScope.launch {
            try {
                val result = doWork()
                future.set(result)
            } catch (t: Throwable) {
                future.setException(t)
            }
        }

        return future
    }

    /**
     * A suspending method to do your work.  This function runs on the coroutine context specified
     * by [coroutineContext].
     * <p>
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [ListenableWorker.Result.failure]
     */
    abstract suspend fun doWork(): Result

    /**
     * Updates the progress for the [CoroutineWorker]. This is a suspending function unlike the
     * [setProgressAsync] API which returns a [ListenableFuture].
     *
     * @param data The progress [Data]
     */
    suspend fun setProgress(data: Data) {
        setProgressAsync(data).await()
    }

    /**
     * Makes the [CoroutineWorker] run in the context of a foreground [android.app.Service]. This
     * is a suspending function unlike the [setForegroundAsync] API which returns a
     * [ListenableFuture].
     *
     * @param foregroundInfo The [ForegroundInfo]
     */
    suspend fun setForeground(foregroundInfo: ForegroundInfo) {
        setForegroundAsync(foregroundInfo).await()
    }

    override fun onStopped() {
        future.cancel(true)
        WorkManager.getInstance(appContext).cancelWorkById(id)
    }
}