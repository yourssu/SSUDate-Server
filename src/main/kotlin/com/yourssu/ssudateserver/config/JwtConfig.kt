package com.yourssu.ssudateserver.config

import com.yourssu.ssudateserver.jwt.component.JwtProvider
import com.yourssu.ssudateserver.jwt.filter.JwtFilter
import com.yourssu.ssudateserver.service.BlackTokenService
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtConfig(
    private val jwtProvider: JwtProvider,
    private val blackTokenService: BlackTokenService,
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(
            JwtFilter(jwtProvider, blackTokenService),
            UsernamePasswordAuthenticationFilter::class.java,
        )
    }
}
