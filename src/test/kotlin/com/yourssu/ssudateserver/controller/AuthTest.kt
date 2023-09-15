package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.AuthRequestDto
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class AuthTest : BaseTest() {
    @Test
    fun authTest() {
        val requestDto = AuthRequestDto(code = validCode)
        val test = mockMvc.post("/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isOk() }
            jsonPath("code") { value(validCode) }
            jsonPath("ticket") { value(1) }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun authTestFailCodeNotFound() {
        val requestDto = AuthRequestDto(code = invalidCode)
        val test = mockMvc.post("/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isNotFound() }
            jsonPath("message") { value("code를 찾을 수 없습니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun authTestFailInvalidCodeLengthUnder10() {
        val requestDto = AuthRequestDto(code = invalidCodeUnder10)
        val test = mockMvc.post("/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("인증코드는 10자리 입니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun authTestFailInvalidCodeLengthOver10() {
        val requestDto = AuthRequestDto(code = invalidCodeOver10)
        val test = mockMvc.post("/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("인증코드는 10자리 입니다.") }
        }
        test.andDo {
            print()
        }
    }
}
