package com.yourssu.ssudateserver.jwt.filter

import com.yourssu.ssudateserver.jwt.component.JwtProvider
import com.yourssu.ssudateserver.jwt.exception.AuthenticateException
import com.yourssu.ssudateserver.service.BlackTokenService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val AUTHORIZATION_HEADER = "Authorization"
private const val AUTHORIZATION_SCHEMA = "Bearer"

class JwtFilter(
    private val jwtProvider: JwtProvider,
    private val blackTokenService: BlackTokenService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)

        if (authorizationHeader == null) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val token = extractAccessTokenFromHeader(authorizationHeader)
            val authentication = jwtProvider.authenticate(token)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (exception: AuthenticateException) {
            request.setAttribute("AuthenticateException", exception)
        }

        filterChain.doFilter(request, response)
    }

    private fun extractAccessTokenFromHeader(authorizationHeader: String): String {
        val splits = authorizationHeader.split(" ")
        validateAccessToken(splits)
        return splits[1]
    }

    private fun validateAccessToken(splits: List<String>) {
        if (splits.size != 2) throw AuthenticateException("잘못된 형식의 Authorization 헤더값 입니다.")
        if (splits[0] != AUTHORIZATION_SCHEMA) throw AuthenticateException("잘못된 Authorization 스키마 입니다.")

        blackTokenService.findBlackToken(splits[1])?.run {
            throw AuthenticateException("사용이 금지된 accessToken 입니다.")
        }
    }
}
