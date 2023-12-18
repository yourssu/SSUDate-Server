package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.redis.OauthCache
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OauthCacheRepository : CrudRepository<OauthCache, String>
