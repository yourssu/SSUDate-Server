package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.dto.request.RefreshTokenRequestDto
import com.yourssu.ssudateserver.dto.request.RegisterCodeRequestDto
import com.yourssu.ssudateserver.dto.request.RegisterFemaleRequestDto
import com.yourssu.ssudateserver.dto.request.RegisterMaleRequestDto
import com.yourssu.ssudateserver.dto.request.UpdateRequestDto
import com.yourssu.ssudateserver.dto.response.LogoutResponseDto
import com.yourssu.ssudateserver.dto.response.RefreshTokenResponseDto
import com.yourssu.ssudateserver.dto.response.RegisterResponseDto
import com.yourssu.ssudateserver.dto.response.UpdateResponseDto
import com.yourssu.ssudateserver.dto.response.UserInfoResponseDto
import com.yourssu.ssudateserver.dto.security.UserPrincipal
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MaleAnimals
import com.yourssu.ssudateserver.exception.logic.AllCanNotRegisterException
import com.yourssu.ssudateserver.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/register/male")
    fun registerMale(
        @Valid @RequestBody
        registerRequestDto: RegisterMaleRequestDto,
    ): RegisterResponseDto {

        if (registerRequestDto.animals == MaleAnimals.ALL) {
            throw AllCanNotRegisterException("ALL은 등록불가능 합니다.")
        }

        return userService.register(
            Animals.valueOf(registerRequestDto.animals.toString()),
            registerRequestDto.nickName,
            registerRequestDto.oauthName,
            registerRequestDto.mbti,
            registerRequestDto.introduce,
            registerRequestDto.contact,
            Gender.MALE,
        )
    }

    @PostMapping("/register/female")
    fun registerFemale(
        @Valid @RequestBody
        registerRequestDto: RegisterFemaleRequestDto,
    ): RegisterResponseDto {
        if (registerRequestDto.animals == FemaleAnimals.ALL) {
            throw AllCanNotRegisterException("ALL은 등록불가능 합니다.")
        }

        return userService.register(
            Animals.valueOf(registerRequestDto.animals.toString()),
            registerRequestDto.nickName,
            registerRequestDto.oauthName,
            registerRequestDto.mbti,
            registerRequestDto.introduce,
            registerRequestDto.contact,
            Gender.FEMALE,
        )
    }

    @PostMapping("/register/code")
    fun registerCode(
        @RequestBody registerRequestDto: RegisterCodeRequestDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): UserInfoResponseDto {
        return userService.registerCode(friendCode = registerRequestDto.code, oauthName = userPrincipal.name)
    }

    @GetMapping("/users/my")
    fun getMyInfo(@AuthenticationPrincipal userPrincipal: UserPrincipal): UserInfoResponseDto {
        return userService.getMyInfo(oauthName = userPrincipal.name)
    }

    @PatchMapping("/users/my")
    fun updateMyInfo(
        @RequestBody updateRequestDto: UpdateRequestDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): UpdateResponseDto {

        return userService.updateUserInfo(
            updateRequestDto.nickName,
            updateRequestDto.mbti,
            updateRequestDto.introduce,
            updateRequestDto.contact,
            userPrincipal.name
        )
    }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): LogoutResponseDto {
        userService.logout(userPrincipal.token, userPrincipal.name)
        return LogoutResponseDto("로그아웃")
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestBody refreshTokenRequestDto: RefreshTokenRequestDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): RefreshTokenResponseDto {
        return userService.refreshToken(refreshTokenRequestDto.refreshToken, userPrincipal.name)
    }
}
