package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.UpdateRequestDto
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.fixture.PrincipalFixture.Companion.setPrincipal
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.patch

@ActiveProfiles("test")
class UpdateUserInfoTest : BaseTest() {
    @Test
    fun updateMaleTest() {
        setPrincipal()

        val requestDto =
            UpdateRequestDto(
                nickName = "NICKNICK",
                mbti = MBTI.INFP,
                introduce = "hihihi",
                contact = "01012345678",
            )

        val test =
            mockMvc.patch("/users/my") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(requestDto)
            }

        test.andExpect {
            status { isOk() }
            jsonPath("id") { exists() }
            jsonPath("animals") { value("PUSSUNG") }
            jsonPath("nickName") { value("NICKNICK") }
            jsonPath("mbti") { value("INFP") }
            jsonPath("introduce") { value("hihihi") }
            jsonPath("contact") { value("01012345678") }
            jsonPath("gender") { value("MALE") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun updateMaleTestFailNickNameDuplicate() {
        setPrincipal()
        val requestDto =
            UpdateRequestDto(
                nickName = "testNick2",
                mbti = MBTI.INFP,
                introduce = "hihihi",
                contact = "01012345678",
            )
        val test =
            mockMvc.patch("/users/my") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(requestDto)
            }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("이미 존재하는 닉네임이에요.") }
        }
        test.andDo {
            print()
        }
    }
}
