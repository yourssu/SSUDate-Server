package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.dto.response.ContactResponseDto
import com.yourssu.ssudateserver.dto.response.SearchContactResponseDto
import com.yourssu.ssudateserver.dto.response.SearchResponseDto
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.exception.logic.DuplicateFollowException
import com.yourssu.ssudateserver.exception.logic.SelfContactException
import com.yourssu.ssudateserver.exception.logic.UserNotFoundException
import com.yourssu.ssudateserver.repository.FollowRepository
import com.yourssu.ssudateserver.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SSUDateService(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
) {

    @Transactional
    fun recentSearch(): List<SearchResponseDto> {
        return userRepository.findTop15ByOrderByCreatedAtDescIdDesc()
            .map { user ->
                SearchResponseDto(
                    animals = user.animals,
                    nickName = user.nickName,
                    mbti = user.mbti,
                    introduce = user.introduce,
                    gender = user.gender,
                    weight = user.weight,
                )
            }
    }

    fun search(gender: Gender, animals: Animals): List<SearchResponseDto> {
        return if (animals == Animals.ALL) {
            userRepository.getRandomUserWithGender(gender.toString())
                .map { user ->
                    SearchResponseDto(
                        animals = user.animals,
                        nickName = user.nickName,
                        mbti = user.mbti,
                        introduce = user.introduce,
                        gender = user.gender,
                        weight = user.weight,
                    )
                }
        } else {
            userRepository.getRandomUserWithGenderAndAnimals(gender.toString(), animals.toString())
                .map { user ->
                    SearchResponseDto(
                        animals = user.animals,
                        nickName = user.nickName,
                        mbti = user.mbti,
                        introduce = user.introduce,
                        gender = user.gender,
                        weight = user.weight,
                    )
                }
        }
    }

    fun searchContact(oauthName: String): List<SearchContactResponseDto> {
        val user =
            userRepository.findByOauthName(oauthName) ?: throw UserNotFoundException("해당 oauthName인 유저가 없습니다.")

        val toUserIdList: List<Long> = followRepository.findAllByFromUserId(user.id!!).map { it.toUserId }

        return userRepository.findAllByIdIn(toUserIdList)
            .map {
                SearchContactResponseDto(
                    animals = it.animals,
                    nickName = it.nickName,
                    mbti = it.mbti,
                    introduce = it.introduce,
                    gender = it.gender,
                    contact = it.contact,
                    weight = it.weight,
                )
            }
    }

    @Transactional
    fun contact(oauthName: String, nickName: String): ContactResponseDto {
        val fromUser =
            userRepository.findByOauthName(oauthName) ?: throw UserNotFoundException("해당 oauthName인 유저가 없습니다.")

        if (fromUser.nickName == nickName) {
            throw SelfContactException("본인의 프로필은 조회할 수 없어요.")
        }

        val toUser = userRepository.findByNickName(nickName) ?: throw UserNotFoundException("존재하지 않는 닉네임이에요.")

        followRepository.findByFromUserIdAndToUserId(fromUserId = fromUser.id!!, toUserId = toUser.id!!)?.run {
            throw DuplicateFollowException("이미 조회한 프로필이에요.")
        }

        followRepository.save(fromUser.contactTo(toUser))

        return ContactResponseDto(
            nickName = toUser.nickName,
            contact = toUser.contact,
        )
    }
}
