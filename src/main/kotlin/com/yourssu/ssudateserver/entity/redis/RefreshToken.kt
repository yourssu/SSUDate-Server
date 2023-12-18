package com.yourssu.ssudateserver.entity.redis

import org.springframework.data.redis.core.RedisHash
import javax.persistence.Id

@RedisHash(value = "jwtToken", timeToLive = 604800000)
data class RefreshToken(
    @Id
    val id: String,
    val refreshToken: String,
)
