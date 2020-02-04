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
//                postId: "5e36e1f8b0c07e397fb46a19",
//                userId: "5e355d3d916bc36dd7dc4306",
//                content: "Comment test 2"
//        )
//        commentService.save(comment)
    }
}
