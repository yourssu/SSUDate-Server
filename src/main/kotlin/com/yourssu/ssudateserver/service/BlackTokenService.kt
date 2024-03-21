package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.redis.BlackToken
import com.yourssu.ssudateserver.repository.BlackTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BlackTokenService(private val repository: BlackTokenRepository) {
    fun saveBlackTokenInfo(accessToken: String) {
        repository.save(BlackToken(accessToken))
    }

    fun findBlackToken(accessToken: String): BlackToken? {
        return repository.findById(accessToken).orElse(null)
    }
}
