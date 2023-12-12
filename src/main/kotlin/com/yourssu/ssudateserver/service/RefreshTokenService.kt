package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.entity.RefreshToken
import com.yourssu.ssudateserver.repository.RefreshTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class RefreshTokenService(private val repository: RefreshTokenRepository) {

    fun saveTokenInfo(oauthName: String, refreshToken: String) {
        repository.save(RefreshToken(oauthName, refreshToken))
    }

    fun findRefreshToken(oauthName: String) : RefreshToken?{
        return repository.findById(oauthName).orElse(null)
    }

    fun removeRefreshToken(oauthName: String) {
        repository.findById(oauthName)
            .ifPresent { repository.delete(it) }
    }
}
