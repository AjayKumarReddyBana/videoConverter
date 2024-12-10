package com.mizutest.demo.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Basic thread pool configuration
        executor.setCorePoolSize(5); // Minimum number of threads always kept alive
        executor.setMaxPoolSize(10); // Maximum number of threads in the pool
        executor.setQueueCapacity(25); // Maximum tasks in the queue before rejecting
        executor.setThreadNamePrefix("VideoProcessing-"); // Prefix for thread names
        executor.initialize(); // Initialize the thread pool

        return executor;
    }
}
