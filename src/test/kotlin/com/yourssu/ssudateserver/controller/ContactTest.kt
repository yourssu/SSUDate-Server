package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.ContactRequestDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class ContactTest : BaseTest() {
    @Test
    fun contactTest() {
        val requestDto = ContactRequestDto(
            code = validCode,
            nickName = "testNick1",
        )
        val test = mockMvc.post("/contact") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }
        test.andExpect {
            status { isOk() }
            jsonPath("nickName") { value("testNick1") }
            jsonPath("contact") { value("Contact1") }
        }
        test.andDo {
            print()
        }

        Assertions.assertThat(authRepository.findByCode("test100000")!!.ticket).isEqualTo(0)
        Assertions.assertThat(userRepository.findByNickName("testNick1")!!.weight).isEqualTo(1)
    }

    @Test
    fun contactTestFailCodeNotFound() {
        val requestDto = ContactRequestDto(
            code = invalidCode,
            nickName = "testNick1",
        )
        val test = mockMvc.post("/contact") {
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
    fun contactTestFailInvalidCodeLengthUnder10() {
        val requestDto = ContactRequestDto(
            code = invalidCodeUnder10,
            nickName = "testNick1",
        )
        val test = mockMvc.post("/contact") {
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
    fun contactTestFailInvalidCodeLengthOver10() {
        val requestDto = ContactRequestDto(
            code = invalidCodeOver10,
            nickName = "testNick1",
        )
        val test = mockMvc.post("/contact") {
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
    fun contactTestFailNotFoundNickName() {
        val requestDto = ContactRequestDto(
            code = validCode,
            nickName = "aaaaa",
        )
        val test = mockMvc.post("/contact") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }
        test.andExpect {
            status { isNotFound() }
            jsonPath("message") { value("NickName인 유저가 없습니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun contactTestFailUnderZeroTicket() {
        val requestDto = ContactRequestDto(
            code = validCode,
            nickName = "testNick1",
        )
        mockMvc.post("/contact") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }
        val test = mockMvc.post("/contact") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }
        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("이용권이 필요한 기능입니다. 이용권 구매 후 사용해주세요!") }
        }
        test.andDo {
            print()
        }
    }
}
