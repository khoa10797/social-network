package com.dangkhoa.socialnetwork.entities.mongo.user

import org.springframework.security.core.GrantedAuthority

class UserAccount extends org.springframework.security.core.userdetails.User {

    UserAccount(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.userName, user.password, user.active, true, true, true, authorities)
        userId = user.userId
        email = user.email
        name = user.name
        dateOfBirth = user.dateOfBirth
        sex = user.gender
        active = user.active
        avatar = user.avatar
    }

    String userId
    String email
    String name
    Long dateOfBirth
    Integer sex
    Boolean active
    String avatar
}
