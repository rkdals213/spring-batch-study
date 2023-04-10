package com.example.springbatchstudy

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringBatchStudyApplication

fun main(args: Array<String>) {
    val exitCode = try {
        SpringApplication.exit(runApplication<SpringBatchStudyApplication>(*args))
    } catch (e: Exception) {
        5
    }

    exitProcess(exitCode)
}
