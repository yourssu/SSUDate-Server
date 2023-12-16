package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.ContactRequestDto
import com.yourssu.ssudateserver.fixture.PrincipalFixture.Companion.setPrincipal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.post

@ActiveProfiles("test")
class ContactTest : BaseTest() {

    @Test
    fun contactTest() {
        setPrincipal()
        val requestDto = ContactRequestDto(
            nickName = "testNick2",
        )

        val user = userRepository.findByNickName("testNick2")!!
        val previousWeight = user.weight
        println(previousWeight)

        val test = mockMvc.post("/contact") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }
        test.andExpect {
            status { isOk() }
            jsonPath("nickName") { value("testNick2") }
            jsonPath("contact") { value("Contact2") }
        }
        test.andDo {
            print()
        }

        assertThat(userRepository.findByNickName("testNick2")!!.weight).isEqualTo(previousWeight + 1)
    }

    @Test
    fun contactTestFailSelfContact() {
        setPrincipal()

        val requestDto = ContactRequestDto(
            nickName = "testNick1",
        )

        val user = userRepository.findByNickName("testNick1")!!

        val test = mockMvc.post("/contact") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }
        test.andExpect {
            jsonPath("message") { value("본인의 nickName으로 Contact할 수 없습니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun contactTestFailNotFoundNickName() {
        setPrincipal()
        val requestDto = ContactRequestDto(
            nickName = "aaaaa",
        )
        val test = mockMvc.post("/contact") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }
        test.andExpect {
            status { isNotFound() }
            jsonPath("message") { value("nickName인 유저가 없습니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun contactTestFailUnderZeroTicket() {
        setPrincipal("oauthName3")
        val requestDto = ContactRequestDto(
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
        }
        test.andDo {
            print()
        }
    }
}
