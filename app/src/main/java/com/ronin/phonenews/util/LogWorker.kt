package com.ronin.phonenews.util

import android.util.Log
import androidx.work.Worker

class LogWorker : Worker() {

    val TAG = LogWorker::javaClass.name

    override fun doWork(): WorkerResult {
        Log.e(TAG, "doWork...")
        return WorkerResult.SUCCESS
    }

}