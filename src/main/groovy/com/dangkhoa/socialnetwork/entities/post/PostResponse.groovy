package com.dangkhoa.socialnetwork.entities.post

import com.dangkhoa.socialnetwork.entities.user.UserResponse

class PostResponse {

    String postId
    String userId
    String postType
    String title
    String content
    List<String> images
    Integer numberLike
    Integer numberDislike
    UserResponse owner
    Long createdAt
    Long updatedAt

}
