package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.common.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

class SearchTest : BaseTest() {
    @Test
    fun searchTestFirstPage() {
        val test = mockMvc.get("/search/male/ALL")
        test.andExpect {
            status { isOk() }
            jsonPath("users.size()") { value(16) }
            jsonPath("users[0].nickName") { value("testNick999") }
            jsonPath("users[0].mbti") { value("ESTJ") }
            jsonPath("users[0].introduce") { value("Introduction999") }
            jsonPath("page.size") { value(16) }
            jsonPath("page.totalElements") { value(500) }
            jsonPath("page.totalPages") { value(32) }
            jsonPath("page.number") { value(0) }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun searchTestSecondPage() {
        val test = mockMvc.get("/search/male/ALL?page=1&size=8")
        test.andExpect {
            status { isOk() }
            jsonPath("users.size()") { value(8) }
            jsonPath("users[0].nickName") { value("testNick983") }
            jsonPath("users[0].mbti") { value("ESTJ") }
            jsonPath("users[0].introduce") { value("Introduction983") }
            jsonPath("page.size") { value(8) }
            jsonPath("page.totalElements") { value(500) }
            jsonPath("page.totalPages") { value(63) }
            jsonPath("page.number") { value(1) }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun searchTestLastPage() {
        val test = mockMvc.get("/search/male/ALL?page=62&size=8")
        test.andExpect {
            status { isOk() }
            jsonPath("users.size()") { value(4) }
            jsonPath("users[0].nickName") { value("testNick7") }
            jsonPath("users[0].mbti") { value("ESTJ") }
            jsonPath("users[0].introduce") { value("Introduction7") }
            jsonPath("users[3].nickName") { value("testNick1") }
            jsonPath("users[3].mbti") { value("ENFJ") }
            jsonPath("users[3].introduce") { value("Introduction1") }
            jsonPath("page.size") { value(8) }
            jsonPath("page.totalElements") { value(500) }
            jsonPath("page.totalPages") { value(63) }
            jsonPath("page.number") { value(62) }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun searchTestAnimal() {
        val test = mockMvc.get("/search/female/DOG?size=8")
        test.andExpect {
            status { isOk() }
            jsonPath("users.size()") { value(8) }
            jsonPath("users[0].nickName") { value("testNick992") }
            jsonPath("users[0].mbti") { value("ISTP") }
            jsonPath("users[0].introduce") { value("Introduction992") }
            jsonPath("page.size") { value(8) }
            jsonPath("page.totalElements") { value(100) }
            jsonPath("page.totalPages") { value(13) }
            jsonPath("page.number") { value(0) }
        }
        test.andDo {
            print()
        }
    }

    @Test
    fun searchTestAnimalLastPage() {
        val test = mockMvc.get("/search/female/DOG?page=12&size=8")
        test.andExpect {
            status { isOk() }
            jsonPath("users.size()") { value(4) }
            jsonPath("users[0].nickName") { value("testNick32") }
            jsonPath("users[0].mbti") { value("ISTP") }
            jsonPath("users[0].introduce") { value("Introduction32") }
            jsonPath("users[3].nickName") { value("testNick2") }
            jsonPath("users[3].mbti") { value("ENFP") }
            jsonPath("users[3].introduce") { value("Introduction2") }
            jsonPath("page.size") { value(8) }
            jsonPath("page.totalElements") { value(100) }
            jsonPath("page.totalPages") { value(13) }
            jsonPath("page.number") { value(12) }
        }
        test.andDo {
            print()
        }
    }
}
