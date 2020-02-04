package com.dangkhoa.socialnetwork.entities.comment

import com.dangkhoa.socialnetwork.entities.user.UserResponse

class CommentResponse {

    String commentId
    String postId
    String userId
    String content
    Integer numberLike
    Integer numberDislike
    UserResponse owner
    Long createdAt
    Long updatedAt

}
