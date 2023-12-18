package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.entity.redis.OauthCache
import com.yourssu.ssudateserver.repository.OauthCacheRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OauthCacheService(private val repository: OauthCacheRepository) {
    fun saveOauthName(oauthName: String) {
        repository.save(OauthCache(oauthName))
    }

    fun findOauthName(oauthName: String): OauthCache? {
        return repository.findById(oauthName).orElse(null)
    }

    fun removeOathName(oauthName: String) {
        repository.findById(oauthName)
            .ifPresent { repository.delete(it) }
    }
}
