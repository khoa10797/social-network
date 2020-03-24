package com.dangkhoa.socialnetwork

import com.dangkhoa.socialnetwork.entities.user.User
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.services.UserService
import ma.glasnost.orika.MapperFacade
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService
    @Autowired
    MapperFacade userMapperFacade

    @Test
    void createUser() {
        def user = new User(
                name: 'Ma bư',
                userName: 'admin',
                password: 'admin',
                age: 23,
                avatar: "majinbuu.png"
        )
        userService.save(user)
    }

    @Test
    void userMapper(){

    }
}
