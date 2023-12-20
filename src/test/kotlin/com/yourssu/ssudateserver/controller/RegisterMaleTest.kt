package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.dto.request.RegisterMaleRequestDto
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.enums.MaleAnimals
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.post

@ActiveProfiles("test")
class RegisterMaleTest : BaseTest() {

    @Test
    fun registerMaleTest() {
        val requestDto = RegisterMaleRequestDto(
            animals = MaleAnimals.WOLF,
            nickName = "NICKNICK",
            oauthName = "oauthName",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        oauthCacheService.saveOauthName(requestDto.oauthName)

        val test = mockMvc.post("/register/male") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }

        test.andExpect {
            status { isOk() }
            jsonPath("id") { exists() }
            jsonPath("animals") { value("WOLF") }
            jsonPath("nickName") { value("NICKNICK") }
            jsonPath("mbti") { value("INFP") }
            jsonPath("introduce") { value("hihihi") }
            jsonPath("contact") { value("01012345678") }
            jsonPath("gender") { value("MALE") }
            jsonPath("code") { exists() }
            jsonPath("codeInputChance") { value(1) }
        }
        test.andDo {
            print()
        }

        Assertions.assertThat(userRepository.findByNickName("NICKNICK")!!.weight).isEqualTo(0)
    }

    @Test
    fun registerMaleFailOauthNameNotFoundTest() {
        val requestDto = RegisterMaleRequestDto(
            animals = MaleAnimals.WOLF,
            nickName = "NICKNICK",
            oauthName = "oauthName",
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
            jsonPath("message") { value("해당 oauthName이 존재하지 않습니다.") }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun registerMaleTestFailAnimalsAll() {
        val requestDto = RegisterMaleRequestDto(
            animals = MaleAnimals.ALL,
            nickName = "NICKNICK",
            oauthName = "oauthName",
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
            animals = MaleAnimals.BEAR,
            nickName = "testNick1",
            oauthName = "oauthName",
            mbti = MBTI.INFP,
            introduce = "hihihi",
            contact = "01012345678",
        )
        oauthCacheService.saveOauthName(requestDto.oauthName)

        val test = mockMvc.post("/register/male") {
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
