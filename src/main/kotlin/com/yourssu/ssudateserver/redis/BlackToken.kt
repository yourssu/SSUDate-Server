package com.yourssu.ssudateserver.redis

import org.springframework.data.redis.core.RedisHash
import javax.persistence.Id

@RedisHash(value = "blackToken", timeToLive = 1800000)
data class BlackToken(
    @Id
    val id: String,
)
