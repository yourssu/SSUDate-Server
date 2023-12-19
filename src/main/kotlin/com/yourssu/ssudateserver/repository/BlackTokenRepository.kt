package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.redis.BlackToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BlackTokenRepository : CrudRepository<BlackToken, String>
