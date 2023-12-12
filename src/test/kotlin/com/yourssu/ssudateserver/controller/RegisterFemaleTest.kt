package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.RegisterFemaleRequestDto
import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.MBTI
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.post

@ActiveProfiles("test")
class RegisterFemaleTest : BaseTest() {

    @Test
    fun registerFemaleTest() {
        val requestDto = RegisterFemaleRequestDto(
            animals = FemaleAnimals.CAT,
            nickName = "NICKNICK",
            oauthName = "oauthName",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/female") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isOk() }
            jsonPath("id") { exists() }
            jsonPath("animals") { value("CAT") }
            jsonPath("nickName") { value("NICKNICK") }
            jsonPath("mbti") { value("INFP") }
            jsonPath("introduce") { value("hihihi") }
            jsonPath("contact") { value("01012345678") }
            jsonPath("gender") { value("FEMALE") }
        }
        test.andDo {
            print()
        }

        Assertions.assertThat(userRepository.findByNickName("NICKNICK")!!.weight).isEqualTo(0)
    }

    @Test
    fun registerFemaleTestFailAnimalsAll() {
        val requestDto = RegisterFemaleRequestDto(
            animals = FemaleAnimals.ALL,
            nickName = "NICKNICK",
            oauthName = "oauthName",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/female") {
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
    fun registerFemaleTestFailNickNameDuplicate() {
        val requestDto = RegisterFemaleRequestDto(
            animals = FemaleAnimals.CAT,
            nickName = "testNick1",
            oauthName = "oauthName",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/female") {
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
