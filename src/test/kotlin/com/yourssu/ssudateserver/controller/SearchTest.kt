package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

class SearchTest : BaseTest() {
    @Test
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
}
