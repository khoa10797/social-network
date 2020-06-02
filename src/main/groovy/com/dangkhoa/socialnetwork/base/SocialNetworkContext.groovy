package com.dangkhoa.socialnetwork.base

import com.dangkhoa.socialnetwork.entities.mongo.user.UserAccount
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SocialNetworkContext {

    UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
        if (authentication.getPrincipal() instanceof UserAccount) {
            return (UserAccount) authentication.getPrincipal()
        }
        return null;
    }

}
