package com.dangkhoa.socialnetwork.base

import com.dangkhoa.socialnetwork.entities.user.UserAccount
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SocialNetworkContext {

    UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
        return (UserAccount) authentication.getPrincipal()
    }

}
