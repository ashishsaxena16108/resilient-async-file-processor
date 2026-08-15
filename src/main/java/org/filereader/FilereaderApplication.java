package org.filereader;

import org.filereader.listeners.LogRetryListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableRetry
public class FilereaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilereaderApplication.class, args);
    }
    @Bean
    public RetryTemplate retryTemplate(LogRetryListener logRetryListener) {
        RetryTemplate template = new RetryTemplate();
        template.setListeners(new org.springframework.retry.RetryListener[]{logRetryListener});
        return template;
    }
}
