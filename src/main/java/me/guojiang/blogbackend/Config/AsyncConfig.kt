package me.guojiang.blogbackend.Config

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
open class AsyncConfig : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor? {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5
        executor.maxPoolSize = 15
        executor.setQueueCapacity(500)
        executor.initialize()
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return super.getAsyncUncaughtExceptionHandler()
    }

//    class SpringAsyncUncaughtExceptionHandler : AsyncUncaughtExceptionHandler {
//        override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
//        }
//    }
}