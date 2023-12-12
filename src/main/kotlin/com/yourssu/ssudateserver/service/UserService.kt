package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.entity.User
import com.yourssu.ssudateserver.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
) {

    fun searchUser(oauthName: String): User? {
        return userRepository.findUserByOauthName(oauthName)
    }
}
