package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.RegisterMaleRequestDto
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.enums.MaleAnimals
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class RegisterMaleTest : BaseTest() {
    @Test
    fun registerMaleTest() {
        val requestDto = RegisterMaleRequestDto(
            code = validCode,
            animals = MaleAnimals.WOLF,
            nickName = "NICKNICK",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/male") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isOk() }
            jsonPath("id") { exists() }
            jsonPath("code") { value(validCode) }
            jsonPath("animals") { value("WOLF") }
            jsonPath("nickName") { value("NICKNICK") }
            jsonPath("mbti") { value("INFP") }
            jsonPath("introduce") { value("hihihi") }
            jsonPath("contact") { value("01012345678") }
            jsonPath("gender") { value("MALE") }
        }
        test.andDo {
            print()
        }

        Assertions.assertThat(authRepository.findByCode(validCode)!!.ticket).isEqualTo(0)
        Assertions.assertThat(userRepository.findByNickName("NICKNICK")!!.weight).isEqualTo(0)
    }

    @Test
    fun registerMaleTestFailCodeNotFound() {
        val requestDto = RegisterMaleRequestDto(
            code = invalidCode,
            animals = MaleAnimals.WOLF,
            nickName = "NICKNICK",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/male") {
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
    fun registerMaleTestFailInvalidCodeLengthUnder10() {
        val requestDto = RegisterMaleRequestDto(
            code = invalidCodeUnder10,
            animals = MaleAnimals.WOLF,
            nickName = "NICKNICK",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/male") {
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
    fun registerMaleTestFailInvalidCodeLengthOver10() {
        val requestDto = RegisterMaleRequestDto(
            code = invalidCodeOver10,
            animals = MaleAnimals.WOLF,
            nickName = "NICKNICK",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/male") {
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
    fun registerMaleTestFailAnimalsAll() {
        val requestDto = RegisterMaleRequestDto(
            code = validCode,
            animals = MaleAnimals.ALL,
            nickName = "NICKNICK",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/male") {
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
    fun registerMaleTestFailNickNameDuplicate() {
        val requestDto = RegisterMaleRequestDto(
            code = validCode,
            animals = MaleAnimals.BEAR,
            nickName = "testNick1",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        val test = mockMvc.post("/register/male") {
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

    @Test
    fun registerMaleTestFailUnderZeroTicket() {
        val requestDto = RegisterMaleRequestDto(
            code = validCode,
            animals = MaleAnimals.DOG,
            nickName = "test1",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        mockMvc.post("/register/male") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }
        val test = mockMvc.post("/register/male") {
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
