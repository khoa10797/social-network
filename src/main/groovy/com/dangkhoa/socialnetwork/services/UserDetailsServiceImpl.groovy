package com.dangkhoa.socialnetwork.services


import com.dangkhoa.socialnetwork.entities.user.User
import com.dangkhoa.socialnetwork.entities.user.UserAccount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService
    @Autowired
    RoleGroupService roleGroupService

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username)
        List<GrantedAuthority> authorities = getUserAuthority(user.roleGroupIds)
        return buildUserDetails(user, authorities)
    }

    List<GrantedAuthority> getUserAuthority(List<String> roleIds) {
        Set<GrantedAuthority> roles = new HashSet<>()
        List<String> roleByIds = roleGroupService.getRoleByIds(roleIds)

        roleByIds.each { item -> roles.add(new SimpleGrantedAuthority(item)) }
        return new ArrayList<>(roles)
    }

    UserDetails buildUserDetails(User user, List<GrantedAuthority> authorities) {
        return new UserAccount(user, authorities)
    }
}
