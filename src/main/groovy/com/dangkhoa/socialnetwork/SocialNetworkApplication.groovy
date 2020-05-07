package com.dangkhoa.socialnetwork

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SocialNetworkApplication {

    static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication, args)
    }

}
