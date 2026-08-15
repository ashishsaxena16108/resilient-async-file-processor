package org.filereader.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadExecutorConfig {

    @Bean("fileReadingExecutor")
    public ThreadPoolExecutor getThreadPool(){
        return new ThreadPoolExecutor(10,
                50,
                60000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(6000));
    }
}
