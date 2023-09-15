package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.entity.User
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserRepositoryExtension {
    fun getUserWithAnimals(
        gender: Gender,
        animals: Animals,
        pageable: Pageable
    ): Page<User>
}
