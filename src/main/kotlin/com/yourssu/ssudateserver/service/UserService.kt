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
import com.yourssu.ssudateserver.exception.logic.DuplicateCodeException
import com.yourssu.ssudateserver.exception.logic.InvalidRefreshTokenException
import com.yourssu.ssudateserver.exception.logic.NickNameDuplicateException
import com.yourssu.ssudateserver.exception.logic.RefreshTokenNotFoundException
import com.yourssu.ssudateserver.exception.logic.UserNotFoundException
import com.yourssu.ssudateserver.jwt.component.JwtGenerator
import com.yourssu.ssudateserver.repository.CodeRepository
import com.yourssu.ssudateserver.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class UserService(
    private val refreshTokenService: RefreshTokenService,
    private val oauthCacheService: OauthCacheService,
    private val userRepository: UserRepository,
    private val codeRepository: CodeRepository,
    private val jwtGenerator: JwtGenerator,
    private val blackTokenService: BlackTokenService,
    private val webClientService: WebClientService,
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
            introduce = user.introduce,
            contact = user.contact,
            weight = user.weight,
            ticket = user.ticket,
            gender = user.gender,
            code = user.code,
            codeInputChance = user.codeInputChance,
            createdAt = user.createdAt,
        )
    }

    @Transactional
    fun refreshToken(
        refreshToken: String,
        oauthName: String,
    ): RefreshTokenResponseDto {
        val foundedToken =
            refreshTokenService.findRefreshToken(oauthName)
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
    fun logout(
        accessToken: String,
        oauthName: String,
    ) {
        userRepository.findByOauthName(oauthName) ?: throw UserNotFoundException("해당 oauthName인 유저가 없습니다.")

        refreshTokenService.removeRefreshToken(oauthName)
        blackTokenService.saveBlackTokenInfo(accessToken)
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
        oauthCacheService.findOauthName(oauthName)
            ?: throw UserNotFoundException("해당 oauthName이 존재하지 않습니다.")

        oauthCacheService.removeOathName(oauthName)

        if (userRepository.findByNickName(nickName) != null) {
            throw NickNameDuplicateException("이미 존재하는 닉네임이에요.")
        }

        val saveUser =
            userRepository.save(
                User(
                    animals = animals,
                    mbti = mbti,
                    nickName = nickName,
                    oauthName = oauthName,
                    introduce = introduce,
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
            code = saveUser.code,
            codeInputChance = saveUser.codeInputChance,
            accessToken,
            refreshToken,
        )
    }

    @Transactional
    fun updateUserInfo(
        nickName: String,
        mbti: MBTI,
        introduce: String,
        contact: String,
        oauthName: String,
    ): UpdateResponseDto {
        val user =
            userRepository.findByOauthName(oauthName) ?: throw UserNotFoundException("해당 oauthName인 유저가 없습니다.")

        if (user.nickName != nickName && userRepository.findByNickName(nickName) != null) {
            throw NickNameDuplicateException("이미 존재하는 닉네임이에요.")
        }

        val updatedUser =
            user.updateInfo(
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
            introduce = updatedUser.introduce,
            ticket = updatedUser.ticket,
            contact = updatedUser.contact,
            gender = updatedUser.gender,
            code = updatedUser.code,
            codeInputChance = updatedUser.codeInputChance,
        )
    }

    @Transactional
    fun registerCode(
        friendCode: String,
        oauthName: String,
    ): UserInfoResponseDto {
        val user =
            userRepository.findByOauthName(oauthName) ?: throw UserNotFoundException("해당 oauthName인 유저가 없습니다.")

        val myCode = user.code

        if (myCode == friendCode) {
            throw DuplicateCodeException("내 코드는 등록할 수 없어요.")
        }
        codeRepository.findByFromCodeAndToCode(myCode, friendCode)?.run {
            throw DuplicateCodeException("이미 등록한 친구 코드예요.")
        }
        codeRepository.findByFromCodeAndToCode(friendCode, myCode)?.run {
            throw DuplicateCodeException("친구가 이미 당신의 코드를 등록했어요.")
        }
        val toUser =
            userRepository.findByCode(friendCode)
                ?: throw UserNotFoundException("해당 코드의 유저가 존재하지 않아요.")

        val code = user.registerCode(toUser)
        val updatedUser = userRepository.save(user)
        userRepository.save(toUser)
        codeRepository.save(code)

        return UserInfoResponseDto(
            id = updatedUser.id!!,
            animals = updatedUser.animals,
            nickName = updatedUser.nickName,
            mbti = updatedUser.mbti,
            introduce = updatedUser.introduce,
            weight = updatedUser.weight,
            ticket = updatedUser.ticket,
            contact = updatedUser.contact,
            code = updatedUser.code,
            codeInputChance = updatedUser.codeInputChance,
            gender = updatedUser.gender,
            createdAt = updatedUser.createdAt,
        )
    }

    @Transactional
    fun deleteUser(
        accessToken: String,
        oauthName: String,
    ) {
        val user = userRepository.findByOauthName(oauthName) ?: throw UserNotFoundException("해당 oauthName인 유저가 없습니다.")

        webClientService.deleteUser(oauthName.removePrefix("kakao_"))

        refreshTokenService.removeRefreshToken(oauthName)
        blackTokenService.saveBlackTokenInfo(accessToken)
        userRepository.delete(user)
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
