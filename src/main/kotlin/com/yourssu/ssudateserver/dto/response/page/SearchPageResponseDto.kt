package com.yourssu.ssudateserver.dto.response.page

import com.yourssu.ssudateserver.entity.User
import org.springframework.data.domain.Page

class SearchPageResponseDto(page: Page<User>) {
    val users: List<SearchResponseDto> = page.content.map { user ->
        SearchResponseDto(
            animals = user.animals,
            nickName = user.nickName,
            mbti = user.mbti,
            introduce = user.introduction,
            gender = user.gender,
            createdAt = user.createdAt!!,
            weight = user.weight
        )
    }
    val page = PageInfo(page)
}
