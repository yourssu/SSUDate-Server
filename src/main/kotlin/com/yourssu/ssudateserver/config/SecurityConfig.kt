package com.yourssu.ssudateserver.config

import com.yourssu.ssudateserver.dto.security.KakaoOAuth2Response
import com.yourssu.ssudateserver.dto.security.UserPrincipal
import com.yourssu.ssudateserver.enums.RoleType
import com.yourssu.ssudateserver.jwt.component.JwtGenerator
import com.yourssu.ssudateserver.jwt.component.JwtProvider
import com.yourssu.ssudateserver.service.RefreshTokenService
import com.yourssu.ssudateserver.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val jwtGenerator: JwtGenerator,
    private val userService: UserService,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val refreshTokenService: RefreshTokenService,
) {

    @Bean
    fun filterChain(
        http: HttpSecurity,
        oAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
    ): SecurityFilterChain = http
        .csrf { it.disable() }
        .headers { it.frameOptions().disable() }
        .cors {}
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeRequests {
            it
                .antMatchers("/search/contact", "/users/my").authenticated()
                .antMatchers("/register/**", "/v2/api-docs", "/v3/**", "/swagger-resources/**", "/search/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(JwtConfig(jwtProvider, authenticationEntryPoint))
        }
        .oauth2Login {
            it.userInfoEndpoint { userInfo ->
                userInfo.userService(oAuth2UserService)
            }
            it.successHandler(oAuth2SuccessHandler())
        }
        .build()

    @Bean
    fun oAuth2UserService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> =
        OAuth2UserService<OAuth2UserRequest, OAuth2User> { userRequest ->
            val delegate = DefaultOAuth2UserService()
            val oAuth2User = delegate.loadUser(userRequest)

            val kakaoOAuth2Response: KakaoOAuth2Response = KakaoOAuth2Response.from(oAuth2User.attributes)
            val registrationId = userRequest.clientRegistration.registrationId
            val providerId: String = kakaoOAuth2Response.id.toString()
            val username = registrationId + "_" + providerId

            userService.searchUser(username)?.let { UserPrincipal.from(it) }
                ?: UserPrincipal.of(oauthName = username, role = RoleType.GUEST)
        }

    @Bean
    fun oAuth2SuccessHandler() = object : SimpleUrlAuthenticationSuccessHandler() {
        @Throws(IOException::class, ServletException::class)
        override fun onAuthenticationSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication,
        ) {
            val oAuth2User = authentication.principal as OAuth2User

            if (oAuth2User.authorities.any { it.authority == "ROLE_GUEST" }) {
                val oauthName = oAuth2User.name

                val targetUrl = buildRedirectUrl("http://localhost:8080/", mapOf("oauthName" to oauthName))

                redirectStrategy.sendRedirect(request, response, targetUrl)
            } else {
                val accessToken = jwtGenerator.generateAccessToken(oAuth2User.name)
                val refreshToken = jwtGenerator.generateRefreshToken(oAuth2User.name)

                refreshTokenService.saveTokenInfo(oauthName = oAuth2User.name, refreshToken = refreshToken)

                val targetUrl = buildRedirectUrl(
                    "http://localhost:8080/loginSuccess",
                    mapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
                )
                redirectStrategy.sendRedirect(request, response, targetUrl)
            }
        }
    }

    private fun buildRedirectUrl(baseUri: String, queryParams: Map<String, String>): String =
        UriComponentsBuilder.fromUriString(baseUri).apply {
            queryParams.forEach { (key, value) -> queryParam(key, value) }
        }.build().encode(StandardCharsets.UTF_8).toUriString()
}
