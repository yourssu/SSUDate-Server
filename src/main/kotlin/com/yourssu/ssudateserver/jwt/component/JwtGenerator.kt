package com.yourssu.ssudateserver.jwt.component

import com.yourssu.ssudateserver.jwt.component.JwtKeyProvider.Companion.getKey
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtGenerator(
    private val jwtProperties: JwtProperties,
) {
    fun generateAccessToken(oauthName: String): String {
        val claims =
            mutableMapOf<String, Any>().apply {
                put("oauthName", oauthName)
            }
        return createToken(claims, jwtProperties.accessExpTime)
    }

    fun generateRefreshToken(oauthName: String): String {
        val claims =
            mutableMapOf<String, Any>().apply {
                put("oauthName", oauthName)
            }
        return createToken(claims, jwtProperties.refreshExpTime)
    }

    private fun createToken(
        claims: MutableMap<String, Any>,
        expTime: Int,
    ): String {
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expTime))
            .signWith(getKey(jwtProperties.secretKey), SignatureAlgorithm.HS256)
            .compact()
    }
}
