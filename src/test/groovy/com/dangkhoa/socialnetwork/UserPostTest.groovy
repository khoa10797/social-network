package com.dangkhoa.socialnetwork

import com.dangkhoa.socialnetwork.entities.mongo.userpost.UserPost
import com.dangkhoa.socialnetwork.mongo.services.UserPostService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserPostTest {

    @Autowired
    UserPostService userPostService

    @Test
    void updateLikeStatus() {
        userPostService.save(
                new UserPost(
                        userId: "5e5bc9c6958dba414452a851",
                        postId: "5e5bca0efd1fd8770d812158",
                        userStatus: "like"
                )
        )
    }
}
