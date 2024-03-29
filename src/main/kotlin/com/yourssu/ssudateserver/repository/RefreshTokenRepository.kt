package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.redis.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, String>
