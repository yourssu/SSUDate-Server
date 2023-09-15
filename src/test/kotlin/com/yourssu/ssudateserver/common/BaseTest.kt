package com.yourssu.ssudateserver.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.ssudateserver.entity.Auth
import com.yourssu.ssudateserver.entity.User
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.enums.MaleAnimals
import com.yourssu.ssudateserver.repository.AuthRepository
import com.yourssu.ssudateserver.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
class BaseTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var authRepository: AuthRepository

    @Autowired
    protected lateinit var userRepository: UserRepository

    val validCode = "test100000"
    val invalidCode = "testestest"
    val invalidCodeUnder10 = "testest"
    val invalidCodeOver10 = "testestestest"

    fun createCode() {
        var code = 100000
        repeat(10) {
            authRepository.save(
                Auth(
                    code = "test$code",
                    ticket = 1
                )
            )
            code++
        }
    }

    //
    fun createMockUser() {
        val userList = mutableListOf<User>()

        val currentDateTime = LocalDateTime.now()

        repeat(1000) {
            var animal = Animals.values()[it % (Animals.values().size)]
            val mbti = MBTI.values()[it % (MBTI.values().size)]
            val gender = Gender.values()[it % (Gender.values().size)]

            if (gender == Gender.MALE) {
                animal = Animals.valueOf(MaleAnimals.values()[it % (MaleAnimals.values().size)].toString())
            } else if (gender == Gender.FEMALE) {
                animal = Animals.valueOf(FemaleAnimals.values()[it % (FemaleAnimals.values().size)].toString())
            }

            if (animal == Animals.ALL) {
                animal = Animals.PUSSUNG
            }
            val user = User(
                animals = animal,
                mbti = mbti,
                nickName = "testNick${it + 1}",
                introduction = "Introduction${it + 1}",
                contact = "Contact${it + 1}",
                weight = it % 2,
                createdAt = currentDateTime.plusSeconds(it.toLong()),
                gender = gender
            )
            userList.add(user)
//            println("${user.animals} ${user.createdAt} ${user.weight} ${user.mbti} ${user.gender}")
        }
        userRepository.saveAll(userList)
    }

    @BeforeEach
    fun setUp() {
        createCode()
        createMockUser()
    }

    @AfterEach
    fun setDown() {
        authRepository.deleteAll()
        userRepository.deleteAll()
    }
}
