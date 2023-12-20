package com.yourssu.ssudateserver.redis

import org.springframework.data.redis.core.RedisHash
import javax.persistence.Id

@RedisHash(value = "jwtToken", timeToLive = 604800)
data class RefreshToken(
    @Id
    val id: String,
    val refreshToken: String,
)
