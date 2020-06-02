package com.dangkhoa.socialnetwork

import com.dangkhoa.socialnetwork.entities.mongo.user.User
import com.dangkhoa.socialnetwork.mongo.services.UserService
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
                avatar: "https://firebasestorage.googleapis.com/v0/b/social-network-66b92.appspot.com/o/images%2Fmajinbuu.png?alt=media&token=768267cf-f0f1-498a-b953-ef8f46e89697"
        )
        userService.save(user)
    }

    @Test
    void userMapper(){

    }
}
