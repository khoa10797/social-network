package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.base.response.BaseResponse
import com.dangkhoa.socialnetwork.base.response.ResponseData
import com.dangkhoa.socialnetwork.base.response.ResponseError
import com.dangkhoa.socialnetwork.entities.user.User
import com.dangkhoa.socialnetwork.entities.user.UserAccount
import com.dangkhoa.socialnetwork.entities.user.UserRequest
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.services.JwtService
import com.dangkhoa.socialnetwork.services.UserService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController extends BaseController {

    @Autowired
    MapperFacade userMapperFacade
    @Autowired
    UserService userService
    @Autowired
    JwtService jwtService

    @GetMapping
    ResponseEntity<ResponseData> findUsers(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        List<User> users = userService.findUsers(page)
        List<UserResponse> userResponses = userMapperFacade.mapAsList(users, UserResponse.class)
        ResponseData data = new ResponseData(
                statusCode: 200,
                meta: buildMetaResponse(page, pageSize),
                data: userResponses
        )

        return new ResponseEntity<ResponseData>(data, HttpStatus.OK)
    }

    @GetMapping("/{userId}")
    ResponseEntity<ResponseData> findByUserId(@PathVariable String userId) {
        UserResponse userResponse = userService.getByUserId(userId)
        ResponseData data = new ResponseData(
                statusCode: 200,
                data: userResponse
        )

        return new ResponseEntity<ResponseData>(data, HttpStatus.OK)
    }

    @PostMapping
    ResponseEntity add(@RequestBody @Valid UserRequest userRequest) {
        User user = userMapperFacade.map(userRequest, User.class)
        userService.save(user)
        return new ResponseEntity(HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    ResponseEntity update(@PathVariable String id, @RequestBody @Valid UserRequest userRequest) {
        User user = userMapperFacade.map(userRequest, User.class)
        user.userId = id
        userService.save(user)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{id}")
    ResponseEntity remove(@PathVariable String id) {
        userService.remove(id)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/login")
    ResponseEntity<BaseResponse> login(@RequestBody UserRequest userRequest) {
        if (userService.checkLogin(userRequest.userName, userRequest.password)) {
            String token = jwtService.generateToken(userRequest.userName)
            User user = userService.findByUserName(userRequest.userName)
            UserResponse userResponse = userMapperFacade.map(user, UserResponse.class)
            ResponseData data = new ResponseData(
                    statusCode: 200,
                    data: [
                            access_token: token,
                            user        : userResponse
                    ]
            )
            return new ResponseEntity(data, HttpStatus.OK)
        }
        ResponseError error = new ResponseError(
                statusCode: 400,
                error: "Thông tin tài khoản hoặc mật khẩu không chính xác"
        )
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}
