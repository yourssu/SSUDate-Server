package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.dto.response.AuthResponseDto
import com.yourssu.ssudateserver.dto.response.ContactResponseDto
import com.yourssu.ssudateserver.dto.response.RegisterResponseDto
import com.yourssu.ssudateserver.entity.User
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.exception.logic.CodeNotFoundException
import com.yourssu.ssudateserver.exception.logic.NickNameDuplicateException
import com.yourssu.ssudateserver.exception.logic.UnderZeroTicketException
import com.yourssu.ssudateserver.exception.logic.UserNotFoundException
import com.yourssu.ssudateserver.repository.AuthRepository
import com.yourssu.ssudateserver.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class SSUDateService(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    fun auth(code: String): AuthResponseDto {
        val auth =
            authRepository.findByCode(code) ?: throw CodeNotFoundException("code를 찾을 수 없습니다.")
        return AuthResponseDto(auth.code, auth.ticket)
    }

    fun register(
        code: String,
        animals: Animals,
        nickName: String,
        mbti: MBTI,
        introduce: String,
        contact: String,
        gender: Gender
    ): RegisterResponseDto {
        val auth =
            authRepository.findByCode(code) ?: throw CodeNotFoundException("code를 찾을 수 없습니다.")
        if (auth.ticket <= 0) {
            throw UnderZeroTicketException("이용권 값이 0이하입니다.")
        }
        if (userRepository.findByNickName(nickName) != null) {
            throw NickNameDuplicateException("해당 닉네임은 이미 존재합니다.")
        }
        val saveUser = userRepository.save(
            User(
                animals = animals,
                mbti = mbti,
                nickName = nickName,
                introduction = introduce,
                contact = contact,
                gender = gender,
                createdAt = LocalDateTime.now()
            )
        )
        auth.ticket = auth.ticket - 1

        return RegisterResponseDto(
            saveUser.id!!,
            code,
            animals,
            nickName,
            mbti,
            introduce,
            contact,
            gender
        )
    }

    fun search(gender: Gender, animals: Animals, page: Int, size: Int): Page<User> {
        val pageable = PageRequest.of(page, size)
        return userRepository.getUserWithAnimals(gender, animals, pageable)
    }

    fun contact(code: String, nickName: String): ContactResponseDto {
        val auth =
            authRepository.findByCode(code) ?: throw CodeNotFoundException("code를 찾을 수 없습니다.")
        if (auth.ticket <= 0) {
            throw UnderZeroTicketException("이용권 값이 0이하입니다.")
        }
        val user = userRepository.findByNickName(nickName) ?: throw UserNotFoundException("NickName인 유저가 없습니다.")
        auth.ticket--
        user.weight++
        return ContactResponseDto(user.nickName, user.contact)
    }
}
