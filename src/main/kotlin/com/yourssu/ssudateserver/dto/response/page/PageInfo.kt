package com.yourssu.ssudateserver.dto.response.page

import org.springframework.data.domain.Page

class PageInfo<T>(
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int
) {
    constructor(page: Page<T>) : this(
        size = page.size,
        totalElements = page.totalElements,
        totalPages = page.totalPages,
        number = page.number
    )
}
