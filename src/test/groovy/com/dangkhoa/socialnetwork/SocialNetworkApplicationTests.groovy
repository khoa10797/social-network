package com.dangkhoa.socialnetwork

import com.dangkhoa.socialnetwork.job.TrendingPostTask
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SocialNetworkApplicationTests {

    @Autowired
    TrendingPostTask trendingPostTask

    @Test
    void contextLoads() {
        trendingPostTask.staticsTrendingPost()
    }

}
