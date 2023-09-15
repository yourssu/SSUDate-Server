package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.entity.Auth
import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository : JpaRepository<Auth, Long> {
    fun findByCode(code: String): Auth?
}
