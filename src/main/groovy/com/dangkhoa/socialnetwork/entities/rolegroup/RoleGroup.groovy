package com.dangkhoa.socialnetwork.entities.rolegroup

import org.springframework.data.mongodb.core.mapping.Document

@Document("role_groups")
class RoleGroup {

    String roleGroupId;
    List<String> roles;

}
