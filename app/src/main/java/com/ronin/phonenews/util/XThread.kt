package com.ronin.phonenews.util

import java.util.concurrent.*

/**
 * Created by ronindong on 2017/7/13.
 */

class XThread private constructor() {

    init {
        throw IllegalAccessError("IllegalAccessError") as Throwable
    }

    companion object {
        /**
         * 默认线程池大小
         */
        private val N_THREADS = Integer.MAX_VALUE
        /**
         * 线程池
         */
        private val mService: ExecutorService?

        init {
            mService = ThreadPoolExecutor(0, N_THREADS, 60, TimeUnit.SECONDS,
                    SynchronousQueue<Runnable>())
        }


        /**
         * 线程池执行线程

         * @param r
         */
        fun execute(r: Runnable) {
            mService?.execute(r)
        }


        /**
         * 执行线程并获取返回值

         * @param callable
         * *
         * @param <T>
         * *
         * @return
        </T> */
        fun <T> submit(callable: Callable<T>): T? {
            val task = FutureTask(callable)
            if (mService != null) {
                mService.submit(task)
                mService.shutdown()
            }
            try {
                return task.get()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }

            return null
        }
    }


}
