package com.dangkhoa.socialnetwork.entities.comment

import com.dangkhoa.socialnetwork.entities.user.UserResponse

class CommentResponse {

    String commentId
    String postId
    String userId
    String parentId
    String content
    Integer numberLike
    Integer numberDislike
    UserResponse user
    List<CommentResponse> childComments
    Long createdAt
    Long updatedAt

}
