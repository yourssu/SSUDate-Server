package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.entity.redis.OauthCache
import org.springframework.data.repository.CrudRepository

interface OauthCacheRepository : CrudRepository<OauthCache, String>
