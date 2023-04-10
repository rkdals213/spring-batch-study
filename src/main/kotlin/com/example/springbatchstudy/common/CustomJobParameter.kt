package com.example.springbatchstudy.common

import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JobParameter
class CustomJobParameter(
    val requestDate: LocalDateTime
)

@Configuration
class JobParameterConfiguration {
    @Bean
    @JobScope
    fun jobParameter(@Value("#{jobParameters[requestDate]}") requestDate: String): CustomJobParameter {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val dateTime = LocalDateTime.parse(requestDate, formatter)

        return CustomJobParameter(dateTime)
    }
}