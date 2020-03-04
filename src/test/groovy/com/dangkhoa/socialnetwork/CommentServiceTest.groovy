package com.dangkhoa.socialnetwork

import com.dangkhoa.socialnetwork.entities.comment.Comment
import com.dangkhoa.socialnetwork.services.CommentService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService

    @Test
    void createComment() {
//        def comment = new Comment(
//                postId: "5e5bca0efd1fd8770d812158",
//                userId: "5e5bc9c6958dba414452a851",
//                parentId: "5e5bca7183e54b2e65d7e7c9",
//                content: "Comment test 4"
//        )
        commentService.save(comment)
    }
}
