package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.RegisterCodeRequestDto
import com.yourssu.ssudateserver.fixture.PrincipalFixture.Companion.setPrincipal
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.post

@ActiveProfiles("test")
class RegisterCodeTest : BaseTest() {
    @Test
    fun registerCodeTest() {
        setPrincipal("oauthName1")

        val registerCodeRequestDto = RegisterCodeRequestDto("code2")

        val fromUser = userRepository.findByOauthName("oauthName1")!!
        val toUser = userRepository.findByOauthName("oauthName2")!!
        val previous = toUser.ticket

        val test =
            mockMvc.post("/register/code") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(registerCodeRequestDto)
            }

        test.andExpect {
            status { isOk() }
            jsonPath("ticket") { value(fromUser.ticket + 1) }
        }

        val code =
            codeRepository.findByFromCodeAndToCode(fromCode = fromUser.code, toCode = toUser.code)

        Assertions.assertThat(code).isNotNull
        Assertions.assertThat(userRepository.findByOauthName("oauthName2")!!.ticket).isEqualTo(previous + 1)
    }

    @Test
    fun registerCodeFailMyCodeTest() {
        setPrincipal("oauthName1")

        val registerCodeRequestDto = RegisterCodeRequestDto("code1")

        val test =
            mockMvc.post("/register/code") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(registerCodeRequestDto)
            }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("내 코드는 등록할 수 없어요.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun registerCodeFailUsedCodeTest() {
        setPrincipal("oauthName1")

        val registerCodeRequestDto = RegisterCodeRequestDto("code2")

        mockMvc.post("/register/code") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(registerCodeRequestDto)
        }

        val test =
            mockMvc.post("/register/code") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(registerCodeRequestDto)
            }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("이미 등록한 친구 코드예요.") }
        }

        test.andDo {
            print()
        }
    }

    @Test
    fun registerCodeFailFriendUseMyCodeTest() {
        setPrincipal("oauthName1")

        val registerCodeRequestDto = RegisterCodeRequestDto("code3")

        val test =
            mockMvc.post("/register/code") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(registerCodeRequestDto)
            }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("친구가 이미 당신의 코드를 등록했어요.") }
        }

        test.andDo {
            print()
        }
    }

    @Test
    fun registerCodeFailUnderZeroInputChanceTest() {
        setPrincipal("oauthName1")

        val registerCodeRequestDtoFirst = RegisterCodeRequestDto("code4")
        val registerCodeRequestDtoSecond = RegisterCodeRequestDto("code5")

        mockMvc.post("/register/code") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(registerCodeRequestDtoFirst)
        }

        val test =
            mockMvc.post("/register/code") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(registerCodeRequestDtoSecond)
            }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("코드입력 기회가 남아있지 않아요.") }
        }

        test.andDo {
            print()
        }
    }
}
