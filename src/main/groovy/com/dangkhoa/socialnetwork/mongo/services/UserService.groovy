package com.dangkhoa.socialnetwork.mongo.services

import com.dangkhoa.socialnetwork.base.SocialNetworkContext
import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.mongo.user.User
import com.dangkhoa.socialnetwork.entities.mongo.user.UserAccount
import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse
import com.dangkhoa.socialnetwork.entities.mongo.userfollow.UserFollow
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import com.dangkhoa.socialnetwork.mongo.repositories.UserRepository
import ma.glasnost.orika.MapperFacade
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    SocialNetworkContext socialNetworkContext
    @Autowired
    MapperFacade userMapperFacade
    @Autowired
    UserRepository userRepository
    @Autowired
    UserFollowService userFollowService
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
        UserResponse userResponse = userMapperFacade.map(user, UserResponse.class)
        fillUserStatus(userResponse)
        return userResponse
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
            if (CollectionUtils.isEmpty(user.roleGroupIds)) {
                user.roleGroupIds = Arrays.asList("USER")
            }

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
        oldUser.intro = StringUtils.isBlank(newUser.intro) ? oldUser.intro : newUser.intro
        oldUser.description = StringUtils.isBlank(newUser.description) ? oldUser.description : newUser.description
        oldUser.avatar = StringUtils.isBlank(newUser.avatar) ? oldUser.avatar : newUser.avatar
        oldUser.dateOfBirth = newUser.dateOfBirth == null ? oldUser.dateOfBirth : newUser.dateOfBirth
        oldUser.gender = newUser.gender == null ? oldUser.gender : newUser.gender
        oldUser.active = newUser.active == null ? oldUser.active : newUser.active
        oldUser.roleGroupIds = CollectionUtils.isEmpty(newUser.roleGroupIds) ? oldUser.roleGroupIds : newUser.roleGroupIds
        oldUser.updatedAt = new Date().getTime()
    }

    List<User> findByUserId(List<String> userIds) {
        return userRepository.findByUserId(userIds)
    }

    List<UserResponse> getByUserIds(List<String> userIds) {
        List<User> users = findByUserId(userIds)
        return userMapperFacade.mapAsList(users, UserResponse.class)
    }

    void updateNumberFollow(String userId, Integer quantity) {
        User user = userRepository.findByUserId(userId)
        if (user.numberFollow <= 0 && quantity < 0) {
            return
        }

        userRepository.updateNumberFollow(userId, quantity)
    }

    private void fillUserStatus(UserResponse userResponse) {
        UserAccount currentUser = socialNetworkContext.getCurrentUser()
        if (currentUser == null) {
            return
        }

        UserFollow userFollow = userFollowService.findByUserIdAndFollowedUserId(currentUser.userId, userResponse.userId)
        if (userFollow != null) {
            userResponse.userStatus = userFollow.userStatus
        }
    }

    List<UserResponse> getFollowedByUserId(String userId) {
        List<UserFollow> userFollows = userFollowService.getUserFollowedByUserId(userId)
        List<String> followerUserIds = userFollows.collect { it.followedUserId }

        return getByUserIds(followerUserIds)
    }

    User updateActive(String userId, Boolean active) {
        User user = findByUserId(userId)
        user.active = active

        return save(user)
    }
}
