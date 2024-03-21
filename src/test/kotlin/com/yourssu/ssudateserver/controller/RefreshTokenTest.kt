package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.RefreshTokenRequestDto
import com.yourssu.ssudateserver.fixture.PrincipalFixture.Companion.setPrincipal
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.post

@ActiveProfiles("test")
class RefreshTokenTest : BaseTest() {
    @Test
    fun refreshTokenTest() {
        setPrincipal()

        val refreshTokenRequestDto = RefreshTokenRequestDto("refreshToken")

        val test =
            mockMvc.post("/refresh") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(refreshTokenRequestDto)
            }

        test
            .andExpect {
                status { isOk() }
                jsonPath("accessToken") { exists() }
                jsonPath("refreshToken") { exists() }
            }
    }

    @Test
    fun refreshTokenNotFoundTest() {
        setPrincipal("oauthName2")

        val refreshTokenRequestDto = RefreshTokenRequestDto("refreshToken")

        val test =
            mockMvc.post("/refresh") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(refreshTokenRequestDto)
            }

        test
            .andExpect {
                status { isNotFound() }
                jsonPath("message") { value("유저의 refreshToken이 존재하지 않습니다.") }
            }
    }

    @Test
    fun invalidRefreshTokenTest() {
        setPrincipal()

        val refreshTokenRequestDto = RefreshTokenRequestDto("validToken")

        val test =
            mockMvc.post("/refresh") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(refreshTokenRequestDto)
            }

        test
            .andExpect {
                status { isBadRequest() }
                jsonPath("message") { value("잘못된 refreshToken입니다.") }
            }
    }
}
