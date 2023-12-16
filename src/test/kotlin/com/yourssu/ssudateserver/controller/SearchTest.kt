package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import com.yourssu.ssudateserver.fixture.PrincipalFixture.Companion.setPrincipal
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.get

@ActiveProfiles("test")
class SearchTest : BaseTest() {

    @Test
    @WithMockUser
    fun searchMaleAllTest() {
        val test = mockMvc.get("/search/male/ALL")
        test.andExpect {
            status { isOk() }
            jsonPath("size()") { value(16) }
            for (i in 0 until 16) {
                jsonPath("[$i].gender") { value("MALE") }
            }
        }
        test.andDo {
            print()
        }
    }

    @Test
    @WithMockUser
    fun searchFemaleAllTest() {
        val test = mockMvc.get("/search/female/ALL")
        test.andExpect {
            status { isOk() }
            jsonPath("size()") { value(16) }
            for (i in 0 until 16) {
                jsonPath("[$i].gender") { value("FEMALE") }
            }
        }
        test.andDo {
            print()
        }
    }

    @Test
    @WithMockUser
    fun searchDOGMaleTest() {
        val test = mockMvc.get("/search/male/DOG")
        test.andExpect {
            status { isOk() }
            jsonPath("size()") { value(16) }
            for (i in 0 until 16) {
                jsonPath("[$i].gender") { value("MALE") }
                jsonPath("[$i].animals") { value("DOG") }
            }
        }
        test.andDo {
            print()
        }
    }

    @Test
    @WithMockUser
    fun searchDOGFemaleTest() {
        val test = mockMvc.get("/search/female/DOG")
        test.andExpect {
            status { isOk() }
            jsonPath("size()") { value(16) }
            for (i in 0 until 16) {
                jsonPath("[$i].gender") { value("FEMALE") }
                jsonPath("[$i].animals") { value("DOG") }
            }
        }
        test.andDo {
            print()
        }
    }

    @Test
    @WithMockUser
    fun searchRecentTest() {
        val test = mockMvc.get("/search/recent")
        var count = 0
        test.andExpect {
            status { isOk() }
            jsonPath("size()") { value(15) }
            for (i in 1000 downTo 986) {
                jsonPath("[$count].nickName") { value("testNick$i") }
                count++
            }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun searchContactTest() {
        setPrincipal("oauthName2")

        val test = mockMvc.get("/search/contact")

        test.andExpect {
            status { isOk() }
            jsonPath("size()") { value(2) }
            jsonPath("[0].contact") { exists() }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun getMyInfoTest() {
        setPrincipal()

        val test = mockMvc.get("/users/my")

        test.andExpect {
            status { isOk() }
            jsonPath("id") { exists() }
            jsonPath("animals") { exists() }
            jsonPath("mbti") { exists() }
            jsonPath("nickName") { exists() }
            jsonPath("introduction") { exists() }
            jsonPath("contact") { exists() }
            jsonPath("weight") { exists() }
            jsonPath("ticket") { exists() }
            jsonPath("code") { exists() }

        }
        test.andDo {
            print()
        }
    }
}
