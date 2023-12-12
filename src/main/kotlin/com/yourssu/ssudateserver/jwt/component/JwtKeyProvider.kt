package com.yourssu.ssudateserver.jwt.component

import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key

class JwtKeyProvider {
    companion object {
        fun getKey(key: String): Key {
            val keyBytes = key.toByteArray(StandardCharsets.UTF_8)
            return Keys.hmacShaKeyFor(keyBytes)
        }
    }
}
