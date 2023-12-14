package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.dto.request.ContactRequestDto
import com.yourssu.ssudateserver.dto.response.ContactResponseDto
import com.yourssu.ssudateserver.dto.response.SearchContactResponseDto
import com.yourssu.ssudateserver.dto.response.SearchResponseDto
import com.yourssu.ssudateserver.dto.security.UserPrincipal
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MaleAnimals
import com.yourssu.ssudateserver.service.SSUDateService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class SSUDateController(
    private val ssuDateService: SSUDateService,
) {

    @GetMapping("/search/recent")
    fun searchRecent(): List<SearchResponseDto> {
        return ssuDateService.recentSearch()
    }

    @GetMapping("/search/male/{animals}")
    fun searchMale(
        @PathVariable animals: MaleAnimals,
    ): List<SearchResponseDto> {
        return ssuDateService.search(Gender.MALE, Animals.valueOf(animals.toString()))
    }

    @GetMapping("/search/female/{animals}")
    fun searchFemale(
        @PathVariable animals: FemaleAnimals,
    ): List<SearchResponseDto> {
        return ssuDateService.search(Gender.FEMALE, Animals.valueOf(animals.toString()))
    }

    @GetMapping("/search/contact")
    fun searchContact(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): List<SearchContactResponseDto> {
        return ssuDateService.searchContact(userPrincipal.name)
    }

    @PostMapping("/contact")
    fun getContact(
        @Valid @RequestBody
        contactRequestDto: ContactRequestDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ContactResponseDto {

        return ssuDateService.contact(userPrincipal.name, contactRequestDto.nickName)
    }
}
