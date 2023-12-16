package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.dto.response.RefreshTokenResponseDto
import com.yourssu.ssudateserver.dto.response.RegisterResponseDto
import com.yourssu.ssudateserver.dto.response.UpdateResponseDto
import com.yourssu.ssudateserver.dto.response.UserInfoResponseDto
import com.yourssu.ssudateserver.entity.User
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.enums.RoleType
import com.yourssu.ssudateserver.exception.logic.InvalidRefreshTokenException
import com.yourssu.ssudateserver.exception.logic.NickNameDuplicateException
import com.yourssu.ssudateserver.exception.logic.RefreshTokenNotFoundException
import com.yourssu.ssudateserver.exception.logic.UserNotFoundException
import com.yourssu.ssudateserver.jwt.component.JwtGenerator
import com.yourssu.ssudateserver.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val refreshTokenService: RefreshTokenService,
    private val jwtGenerator: JwtGenerator,
) {

    fun searchUser(oauthName: String): User? {
        return userRepository.findByOauthName(oauthName)
    }

    fun getMyInfo(oauthName: String): UserInfoResponseDto {
        val user =
            userRepository.findByOauthName(oauthName) ?: throw UserNotFoundException("해당 oauthName인 유저가 없습니다.")

        return UserInfoResponseDto(
            id = user.id!!,
            animals = user.animals,
            mbti = user.mbti,
            nickName = user.nickName,
            introduction = user.introduction,
            contact = user.contact,
            weight = user.weight,
            ticket = user.ticket,
            gender = user.gender,
            code = user.code,
            createdAt = user.createdAt,
        )
    }

    fun refreshToken(refreshToken: String, oauthName: String): RefreshTokenResponseDto {
        val foundedToken = refreshTokenService.findRefreshToken(oauthName)
            ?: throw RefreshTokenNotFoundException("유저의 refreshToken이 존재하지 않습니다.")

        if (refreshToken != foundedToken.refreshToken) {
            refreshTokenService.removeRefreshToken(oauthName)
            throw InvalidRefreshTokenException("잘못된 refreshToken입니다.")
        }

        val accessToken = jwtGenerator.generateAccessToken(oauthName)
        val newRefreshToken = jwtGenerator.generateRefreshToken(oauthName)

        refreshTokenService.saveTokenInfo(oauthName, newRefreshToken)

        return RefreshTokenResponseDto(accessToken = accessToken, refreshToken = newRefreshToken)
    }

    @Transactional
    fun register(
        animals: Animals,
        nickName: String,
        oauthName: String,
        mbti: MBTI,
        introduce: String,
        contact: String,
        gender: Gender,
    ): RegisterResponseDto {
        if (userRepository.findByNickName(nickName) != null) {
            throw NickNameDuplicateException("해당 닉네임은 이미 존재합니다.")
        }
        val saveUser = userRepository.save(
            User(
                animals = animals,
                mbti = mbti,
                nickName = nickName,
                oauthName = oauthName,
                introduction = introduce,
                contact = contact,
                gender = gender,
                role = RoleType.USER,
                code = generateUniqueReferralCode(),
                createdAt = LocalDateTime.now(),
            ),
        )

        val accessToken = jwtGenerator.generateAccessToken(oauthName)
        val refreshToken = jwtGenerator.generateRefreshToken(oauthName)

        refreshTokenService.saveTokenInfo(oauthName, refreshToken)

        return RegisterResponseDto(
            saveUser.id!!,
            animals,
            nickName,
            mbti,
            introduce,
            saveUser.ticket,
            contact,
            gender,
            accessToken,
            refreshToken,
        )
    }

    @Transactional
    fun updateUserInfo(
        animals: Animals,
        nickName: String,
        mbti: MBTI,
        introduce: String,
        contact: String,
        oauthName: String,
    ): UpdateResponseDto {
        val user =
            userRepository.findByOauthName(oauthName) ?: throw UserNotFoundException("해당 oauthName인 유저가 없습니다.")

        if (user.nickName != nickName && userRepository.findByNickName(nickName) != null) {
            throw NickNameDuplicateException("해당 닉네임은 이미 존재합니다.")
        }

        val updatedUser = user.updateInfo(
            animals = animals,
            nickName = nickName,
            mbti = mbti,
            introduce = introduce,
            contact = contact,
        )

        return UpdateResponseDto(
            id = user.id!!,
            animals = updatedUser.animals,
            nickName = updatedUser.nickName,
            mbti = updatedUser.mbti,
            introduce = updatedUser.introduction,
            ticket = updatedUser.ticket,
            contact = updatedUser.contact,
            gender = updatedUser.gender,
        )
    }

    companion object {
        private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private const val CODE_LENGTH = 12
    }

    private fun generateUniqueReferralCode(): String {
        val random = SecureRandom()

        while (true) {
            val referralCode = StringBuilder()

            repeat(CODE_LENGTH) {
                val randomIndex = random.nextInt(CHARACTERS.length)
                val randomChar = CHARACTERS[randomIndex]
                referralCode.append(randomChar)
            }

            userRepository.findByCode(referralCode.toString()) ?: return referralCode.toString()
        }
    }
}
