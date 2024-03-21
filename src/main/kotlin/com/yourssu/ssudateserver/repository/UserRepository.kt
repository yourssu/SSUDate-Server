package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    fun findByNickName(nickName: String): User?

    @Query(value = "SELECT * FROM users WHERE gender= :gender ORDER BY rand() LIMIT 16", nativeQuery = true)
    fun getRandomUserWithGender(
        @Param("gender") gender: String,
    ): List<User>

    @Query(
        value = "SELECT * FROM users WHERE gender= :gender and animals= :animals ORDER BY rand() LIMIT 16",
        nativeQuery = true,
    )
    fun getRandomUserWithGenderAndAnimals(
        @Param("gender") gender: String,
        @Param("animals") animals: String,
    ): List<User>

    fun findAllByIdIn(ids: List<Long>): List<User>

    fun findByOauthName(oauthName: String): User?

    fun findByCode(code: String): User?

    fun findTop15ByOrderByCreatedAtDescIdDesc(): List<User>
}
