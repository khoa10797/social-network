package com.dangkhoa.socialnetwork


import com.dangkhoa.socialnetwork.entities.mongo.post.Post
import com.dangkhoa.socialnetwork.mongo.services.PostService
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
                userOwnerId: "5e355d3d916bc36dd7dc4306",
                content: "Ma bư đang đọc thần chú",
                images: [
                        "Mabu.jpg"
                ],
                numberLike: 100
        )
        postService.save(post)
    }
}
