package com.dangkhoa.socialnetwork.mongo.services

import com.dangkhoa.socialnetwork.entities.mongo.rolegroup.RoleGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleGroupService {

    @Autowired
    com.dangkhoa.socialnetwork.mongo.repositories.RoleGroupRepository roleGroupRepository

    List<String> getRoleById(String roleGroupId) {
        RoleGroup roleGroup = roleGroupRepository.findById(roleGroupId)
        return roleGroup.roles
    }

    List<String> getRoleByIds(List<String> roleGroupIds) {
        List<RoleGroup> roleGroups = roleGroupRepository.findByIds(roleGroupIds)
        return roleGroups.collect { it.roles }.flatten() as List<String>
    }

    RoleGroup save(RoleGroup roleGroup) {
        return roleGroupRepository.save(roleGroup)
    }

    Long remove(String roleGroupId) {
        return roleGroupRepository.remove(roleGroupId)
    }
}
