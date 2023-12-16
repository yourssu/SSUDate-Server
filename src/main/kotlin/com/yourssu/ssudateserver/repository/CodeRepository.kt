package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.entity.Code
import org.springframework.data.jpa.repository.JpaRepository

interface CodeRepository : JpaRepository<Code, String> {

    fun findByFromCodeAndToCode(fromCode: String, toCode: String): Code?
}
