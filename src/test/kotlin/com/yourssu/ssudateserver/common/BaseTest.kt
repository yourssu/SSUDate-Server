package com.yourssu.ssudateserver.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.ssudateserver.entity.Follow
import com.yourssu.ssudateserver.entity.User
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.enums.MaleAnimals
import com.yourssu.ssudateserver.enums.RoleType
import com.yourssu.ssudateserver.repository.FollowRepository
import com.yourssu.ssudateserver.repository.UserRepository
import com.yourssu.ssudateserver.service.RefreshTokenService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@SpringBootTest
@AutoConfigureMockMvc
class BaseTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var followRepository: FollowRepository

    @Autowired
    lateinit var tokenService: RefreshTokenService

    //
    private fun createMockUser() {
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
                oauthName = "oauthName${it + 1}",
                introduction = "Introduction${it + 1}",
                contact = "Contact${it + 1}",
                weight = it % 2,
                createdAt = currentDateTime.plusSeconds(it.toLong()),
                gender = gender,
                role = RoleType.USER,
                ticket = (it + 1) % 3,
            )
            userList.add(user)
//            println("${user.animals} ${user.createdAt} ${user.weight} ${user.mbti} ${user.gender}")
        }
        val saveAll = userRepository.saveAll(userList)

        val user = saveAll[1]

        followRepository.save(
            Follow(fromUserId = user.id!!, toUserId = user.id!! + 1L, createdAt = now())
        )
        followRepository.save(
            Follow(fromUserId = user.id!!, toUserId = user.id!! + 2L, createdAt = now())
        )

        tokenService.saveTokenInfo(oauthName = "oauthName1", "refreshToken")
    }

    @BeforeEach
    fun setUp() {
        createMockUser()
    }

    @AfterEach
    fun setDown() {
        userRepository.deleteAll()
        followRepository.deleteAll()
    }
}
