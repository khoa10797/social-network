package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User as SpringUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username)
        List<GrantedAuthority> authorities = getUserAuthority(user.roleIds)
        return builUserDetails(user, authorities)
    }

    List<GrantedAuthority> getUserAuthority(List<String> roleIds) {
        Set<GrantedAuthority> roles = new HashSet<>()
        roleIds.each { item -> roles.add(new SimpleGrantedAuthority(item)) }
        return new ArrayList<>(roles)
    }

    UserDetails builUserDetails(User user, List<GrantedAuthority> authorities) {
        return new SpringUser(user.userName, user.password, user.active, true, true, true, authorities)
    }
}
