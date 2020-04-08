package com.dangkhoa.socialnetwork.entities.comment

import com.dangkhoa.socialnetwork.entities.user.UserResponse

class CommentResponse {

    String commentId
    String postId
    String userOwnerId
    String parentId
    String content
    Integer numberLike
    Integer numberDislike
    UserResponse userOwner
    List<CommentResponse> childComments
    String userStatus
    Long createdAt
    Long updatedAt

}
