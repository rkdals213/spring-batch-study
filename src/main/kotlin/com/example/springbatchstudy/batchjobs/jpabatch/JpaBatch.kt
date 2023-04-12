package com.example.springbatchstudy.batchjobs.jpabatch

import com.example.springbatchstudy.common.CustomJobListener
import com.example.springbatchstudy.entity.Department
import jakarta.persistence.EntityManagerFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class JpaBatch(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val customJobListener: CustomJobListener,
    private val customStepListener: CustomJobListener,
) {

    private val logger: Logger = LoggerFactory.getLogger(JpaBatch::class.java)

    @Bean
    fun jpaBatchJob(): Job {
        return JobBuilder("jpaBatchJob", jobRepository)
            .start(jpaBatchStep())
            .listener(customJobListener)
            .build()
    }

    @Bean
    @JobScope
    fun jpaBatchStep(): Step {
        return StepBuilder("jpaBatchStep", jobRepository)
            .chunk<Department, Department>(chunkSize, transactionManager)
            .listener(customStepListener)
            .reader(jpaBatchReader())
            .writer(jpaBatchWriter())
            .build()
    }

    @Bean
    @StepScope
    fun jpaBatchReader(): JpaPagingItemReader<Department> {
        return JpaPagingItemReaderBuilder<Department>()
            .name("jpaBatchReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select d from Department d")
            .build()
    }

    @Bean
    @StepScope
    fun jpaBatchWriter(): ItemWriter<Department> {
        return ItemWriter { list: Chunk<out Department> ->
            list.forEach { logger.info(it.toString()) }
        }
    }

    companion object {
        const val chunkSize = 3
    }
}