package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.dto.request.AuthRequestDto
import com.yourssu.ssudateserver.dto.request.ContactRequestDto
import com.yourssu.ssudateserver.dto.request.RegisterFemaleRequestDto
import com.yourssu.ssudateserver.dto.request.RegisterMaleRequestDto
import com.yourssu.ssudateserver.dto.response.AuthResponseDto
import com.yourssu.ssudateserver.dto.response.ContactResponseDto
import com.yourssu.ssudateserver.dto.response.RegisterResponseDto
import com.yourssu.ssudateserver.dto.response.page.SearchPageResponseDto
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MaleAnimals
import com.yourssu.ssudateserver.service.SSUDateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class SSUDateController(private val ssuDateService: SSUDateService) {
    @PostMapping("/auth")
    fun auth(@Valid @RequestBody authRequestDto: AuthRequestDto): AuthResponseDto {
        return ssuDateService.auth(authRequestDto.code)
    }

    @PostMapping("/register/male")
    fun registerMale(@Valid @RequestBody registerRequestDto: RegisterMaleRequestDto): RegisterResponseDto {
        return ssuDateService.register(
            registerRequestDto.code,
            Animals.valueOf(registerRequestDto.animals.toString()),
            registerRequestDto.nickName,
            registerRequestDto.mbti,
            registerRequestDto.introduce,
            registerRequestDto.contact,
            Gender.MALE
        )
    }

    @PostMapping("/register/female")
    fun registerFemale(@Valid @RequestBody registerRequestDto: RegisterFemaleRequestDto): RegisterResponseDto {
        return ssuDateService.register(
            registerRequestDto.code,
            Animals.valueOf(registerRequestDto.animals.toString()),
            registerRequestDto.nickName,
            registerRequestDto.mbti,
            registerRequestDto.introduce,
            registerRequestDto.contact,
            Gender.FEMALE
        )
    }

    @GetMapping("/search/male/{animals}")
    fun searchMale(
        @PathVariable animals: MaleAnimals,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) size: Int?
    ): SearchPageResponseDto {
        val page = ssuDateService.search(Gender.MALE, Animals.valueOf(animals.toString()), page ?: 0, size ?: 16)
        return SearchPageResponseDto(page)
    }

    @GetMapping("/search/female/{animals}")
    fun searchFemale(
        @PathVariable animals: FemaleAnimals,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) size: Int?
    ): SearchPageResponseDto {
        val page = ssuDateService.search(Gender.FEMALE, Animals.valueOf(animals.toString()), page ?: 0, size ?: 16)
        return SearchPageResponseDto(page)
    }

    @PostMapping("/contact")
    fun getContact(@Valid @RequestBody contactRequestDto: ContactRequestDto): ContactResponseDto {
        return ssuDateService.contact(contactRequestDto.code, contactRequestDto.nickName)
    }
}
