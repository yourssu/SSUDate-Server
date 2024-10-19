package com.yourssu.ssudateserver.jwt.component

import com.yourssu.ssudateserver.jwt.component.JwtKeyProvider.Companion.getKey
import com.yourssu.ssudateserver.jwt.exception.AuthenticateException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.stereotype.Component

@Component
class JwtExtractor(
    private val jwtProperties: JwtProperties,
) {
    fun extractOAuthName(token: String): String =
        extractAllClaims(token)["oauthName", String::class.java]
            ?: throw AuthenticateException("JWT 토큰에 oauthName 클레임이 없습니다.")

    private fun extractAllClaims(token: String): Claims {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(getKey(jwtProperties.secretKey))
                .build()
                .parseClaimsJws(token)
                .body
        } catch (expiredJwtException: ExpiredJwtException) {
            throw AuthenticateException("만료된 Jwt 토큰입니다.")
        } catch (unsupportedJwtException: UnsupportedJwtException) {
            throw AuthenticateException("지원되지 않는 Jwt 토큰입니다.")
        } catch (malformedJwtException: MalformedJwtException) {
            throw AuthenticateException("잘못된 형식의 Jwt 토큰입니다.")
        } catch (signatureException: SignatureException) {
            throw AuthenticateException("잘못된 Jwt Signature 값입니다.")
        } catch (illegalArgumentException: IllegalArgumentException) {
            throw AuthenticateException("질못된 Jwt 헤더 값입니다.")
        }
    }
}
