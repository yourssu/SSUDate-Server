package com.yourssu.ssudateserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class SsuDateServerApplication

fun main(args: Array<String>) {
    runApplication<SsuDateServerApplication>(*args)
}
