package com.dangkhoa.socialnetwork.entities.post

import com.dangkhoa.socialnetwork.entities.user.UserResponse

class PostResponse {

    String postId
    String userOwnerId
    String topicId
    String postType
    String title
    String content
    List<String> images
    Integer numberLike
    Integer numberDislike
    UserResponse userOwner
    Long numberComment
    String userStatus
    Long createdAt
    Long updatedAt

}
