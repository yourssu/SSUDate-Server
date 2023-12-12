package com.yourssu.ssudateserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SsuDateServerApplication

fun main(args: Array<String>) {
    runApplication<SsuDateServerApplication>(*args)
}
