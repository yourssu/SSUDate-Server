package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.UpdateFemaleRequestDto
import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.fixture.PrincipalFixture.Companion.setPrincipal
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.patch

@ActiveProfiles("test")
class UpdateFemaleTest : BaseTest() {

    @Test
    fun updateMaleTest() {
        setPrincipal("oauthName2")

        val requestDto = UpdateFemaleRequestDto(
            animals = FemaleAnimals.FOX,
            nickName = "NICKNICK",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )

        val test = mockMvc.patch("/users/my/female") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isOk() }
            jsonPath("id") { exists() }
            jsonPath("animals") { value("FOX") }
            jsonPath("nickName") { value("NICKNICK") }
            jsonPath("mbti") { value("INFP") }
            jsonPath("introduce") { value("hihihi") }
            jsonPath("contact") { value("01012345678") }
            jsonPath("gender") { value("FEMALE") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun updateMaleTestFailAnimalsAll() {
        setPrincipal("oauthName2")
        val requestDto = UpdateFemaleRequestDto(
            animals = FemaleAnimals.ALL,
            nickName = "NICKNICK",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.patch("/users/my/female") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("ALL은 등록불가능 합니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun updateMaleTestFailNickNameDuplicate() {
        setPrincipal("oauthName2")
        val requestDto = UpdateFemaleRequestDto(
            animals = FemaleAnimals.FOX,
            nickName = "testNick1",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.patch("/users/my/female") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isBadRequest() }
            jsonPath("message") { value("해당 닉네임은 이미 존재합니다.") }
        }
        test.andDo {
            print()
        }
    }
}
