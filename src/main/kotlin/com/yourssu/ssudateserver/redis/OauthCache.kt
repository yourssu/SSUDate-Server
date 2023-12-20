package com.yourssu.ssudateserver.redis

import org.springframework.data.redis.core.RedisHash
import javax.persistence.Id

@RedisHash(value = "oauthCache", timeToLive = 3600)
data class OauthCache(
    @Id
    val id: String
)
