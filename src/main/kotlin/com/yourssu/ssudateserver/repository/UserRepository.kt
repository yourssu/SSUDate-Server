package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserRepositoryExtension {
    fun findByNickName(nickName: String): User?
}
