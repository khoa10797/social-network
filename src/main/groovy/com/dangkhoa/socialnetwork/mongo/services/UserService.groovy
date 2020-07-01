package com.dangkhoa.socialnetwork.mongo.services

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.mongo.user.User
import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import ma.glasnost.orika.MapperFacade
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    MapperFacade userMapperFacade
    @Autowired
    com.dangkhoa.socialnetwork.mongo.repositories.UserRepository userRepository
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder

    Boolean checkLogin(String userName, String password) {
        User user = findByUserName(userName)
        if (user == null)
            return false

        return bCryptPasswordEncoder.matches(password, user.password)
    }

    List<User> findUsers(Integer page, Integer limit) {
        return userRepository.findUsers(limit ?: Constant.DEFAULT_PAGE_SIZE, page ?: 1)
    }

    List<User> findUsers(Integer page) {
        return findUsers(page, Constant.DEFAULT_PAGE_SIZE)
    }

    User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
    }

    UserResponse getByUserId(String userId) {
        User user = findByUserId(userId)
        return userMapperFacade.map(user, UserResponse.class)
    }

    Long remove(String userId) {
        return userRepository.remove(userId)
    }

    User findByUserName(String userName) {
        return userRepository.findByUserName(userName)
    }

    User findByEmail(String email) {
        return userRepository.findByEmail(email)
    }

    User save(User user) {
        if (StringUtils.isBlank(user.userId)) {
            user.userId = new ObjectId().toString()
            user.password = bCryptPasswordEncoder.encode(user.password)
            user.active = true
            user.createdAt = new Date().getTime()
            user.updatedAt = new Date().getTime()
            return userRepository.save(user)
        }

        User existUser = findByUserId(user.userId)
        if (existUser == null)
            throw new InValidObjectException("user not found")

        setUpdateValue(existUser, user)
        return userRepository.save(existUser)
    }

    void setUpdateValue(User oldUser, User newUser) {
        oldUser.name = StringUtils.isBlank(newUser.name) ? oldUser.name : newUser.name
        oldUser.dateOfBirth = newUser.dateOfBirth == null ? oldUser.dateOfBirth : newUser.dateOfBirth
        oldUser.gender = newUser.gender == null ? oldUser.gender : newUser.gender
        oldUser.roleGroupIds = newUser.roleGroupIds == null ? oldUser.roleGroupIds : newUser.roleGroupIds
        oldUser.password = StringUtils.isBlank(newUser.password) ? oldUser.password : bCryptPasswordEncoder.encode(newUser.password)
        oldUser.updatedAt = new Date().getTime()
    }

    List<User> findByUserId(List<String> userIds) {
        return userRepository.findByUserId(userIds)
    }

    List<UserResponse> getByUserIds(List<String> userIds) {
        List<User> users = findByUserId(userIds)
        return userMapperFacade.mapAsList(users, UserResponse.class)
    }
}
