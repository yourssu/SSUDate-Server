package com.yourssu.ssudateserver.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "access_log")
class AccessLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val ip: String?,

    val os: String?,
    @Column(name = "request_url")
    val requestURL: String,

    @Column(name = "method")
    val method: String,
    @Column(name = "request_body")
    val requestBody: String?,

    @Column(columnDefinition = "TEXT", name = "response_body")
    val responseBody: String?,

    @Column(name = "created_at")
    val createdAt: LocalDateTime
)
