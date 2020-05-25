package com.dangkhoa.socialnetwork

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

import java.util.concurrent.Executor

@SpringBootApplication
@EnableScheduling
@EnableAsync
class SocialNetworkApplication {

    static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication, args)
    }

    @Bean
    Executor executor() {
        return new SimpleAsyncTaskExecutor()
    }
}
