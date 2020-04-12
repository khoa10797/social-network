package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.rolegroup.RoleGroup
import com.dangkhoa.socialnetwork.repositories.RoleGroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleGroupService {

    @Autowired
    RoleGroupRepository roleGroupRepository

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
