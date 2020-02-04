package com.dangkhoa.socialnetwork

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.services.PostService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService

    @Test
    void createPost() {
        def post = new Post(
                userId: "5e355d3d916bc36dd7dc4306",
                postType: Constant.BLOG,
                content: "Ma bư đang đọc thần chú",
                images: [
                        "../assets/images/majinbuu.png"
                ],
                numberLike: 100
        )
        postService.save(post)
    }
}
