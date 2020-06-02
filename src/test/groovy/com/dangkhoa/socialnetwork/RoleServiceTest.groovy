package com.dangkhoa.socialnetwork

import com.dangkhoa.socialnetwork.entities.mongo.rolegroup.RoleGroup
import com.dangkhoa.socialnetwork.mongo.services.RoleGroupService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RoleServiceTest {

    @Autowired
    RoleGroupService roleGroupService

    @Test
    void createRoleGroup() {
        roleGroupService.save(
                new RoleGroup(
                        roleGroupId: "USER",
                        roles: [
                                "ROLE_POST",
                                "ROLE_COMMENT"
                        ]
                )
        )
    }

}
