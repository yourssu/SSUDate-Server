package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.RegisterFemaleRequestDto
import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.MBTI
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class RegisterFemaleTest : BaseTest() {
    @Test
    fun registerFemaleTest() {
        val requestDto = RegisterFemaleRequestDto(
            code = validCode,
            animals = FemaleAnimals.CAT,
            nickName = "NICKNICK",
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
            jsonPath("code") { value(validCode) }
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

        Assertions.assertThat(authRepository.findByCode(validCode)!!.ticket).isEqualTo(0)
        Assertions.assertThat(userRepository.findByNickName("NICKNICK")!!.weight).isEqualTo(0)
    }

    @Test
    fun registerFemaleTestFailCodeNotFound() {
        val requestDto = RegisterFemaleRequestDto(
            code = invalidCode,
            animals = FemaleAnimals.CAT,
            nickName = "NICKNICK",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/female") {
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
    fun registerFemaleTestFailInvalidCodeLengthUnder10() {
        val requestDto = RegisterFemaleRequestDto(
            code = invalidCodeUnder10,
            animals = FemaleAnimals.CAT,
            nickName = "NICKNICK",
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
            jsonPath("message") { value("인증코드는 10자리 입니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun registerFemaleTestFailInvalidCodeLengthOver10() {
        val requestDto = RegisterFemaleRequestDto(
            code = invalidCodeOver10,
            animals = FemaleAnimals.CAT,
            nickName = "NICKNICK",
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
            jsonPath("message") { value("인증코드는 10자리 입니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun registerFemaleTestFailAnimalsAll() {
        val requestDto = RegisterFemaleRequestDto(
            code = validCode,
            animals = FemaleAnimals.ALL,
            nickName = "NICKNICK",
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
            code = validCode,
            animals = FemaleAnimals.CAT,
            nickName = "testNick1",
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
