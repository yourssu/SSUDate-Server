package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.RegisterCodeRequestDto
import com.yourssu.ssudateserver.fixture.PrincipalFixture.Companion.setPrincipal
import com.yourssu.ssudateserver.repository.CodeRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.post

@ActiveProfiles("test")
class RegisterCodeTest : BaseTest() {

    @Autowired
    lateinit var codeRepository: CodeRepository

    @Test
    fun registerCodeTest() {
        setPrincipal("oauthName1")

        val registerCodeRequestDto = RegisterCodeRequestDto("code2")

        val fromUser = userRepository.findByOauthName("oauthName1")!!
        val toUser = userRepository.findByOauthName("oauthName2")!!
        val previous = toUser.ticket

        val test = mockMvc.post("/register/code") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(registerCodeRequestDto)
        }

        test
            .andExpect {
                status { isOk() }
                jsonPath("ticket") { value(fromUser.ticket + 1) }
            }

        val code =
            codeRepository.findByFromCodeAndToCode(fromCode = fromUser.code, toCode = toUser.code)

        Assertions.assertThat(code).isNotNull
        Assertions.assertThat(userRepository.findByOauthName("oauthName2")!!.ticket).isEqualTo(previous + 1)
    }
}
