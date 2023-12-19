package com.yourssu.ssudateserver.jwt.component

import com.yourssu.ssudateserver.dto.security.UserPrincipal
import com.yourssu.ssudateserver.enums.RoleType
import com.yourssu.ssudateserver.jwt.exception.AuthenticateException
import com.yourssu.ssudateserver.service.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class JwtProvider(
    private val userService: UserService,
    private val jwtExtractor: JwtExtractor,
) {

    fun authenticate(token: String): Authentication {
        val oauthName = jwtExtractor.extractOAuthName(token)
        val user = userService.searchUser(oauthName)
            ?: throw AuthenticateException("Jwt 토큰에 해당하는 유저가 존재하지 않습니다.")

        return UsernamePasswordAuthenticationToken(
            UserPrincipal.from(user, token),
            "",
            listOf(SimpleGrantedAuthority(RoleType.USER.name)),
        )
    }
}
