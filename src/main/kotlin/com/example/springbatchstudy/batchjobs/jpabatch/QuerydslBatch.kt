package com.example.springbatchstudy.batchjobs.jpabatch

import com.example.springbatchstudy.common.CustomJobListener
import com.example.springbatchstudy.common.QuerydslPagingItemReader
import com.example.springbatchstudy.common.QuerydslZeroPagingItemReader
import com.example.springbatchstudy.entity.Department
import com.example.springbatchstudy.entity.QDepartment
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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class QuerydslBatch(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val customJobListener: CustomJobListener,
    private val customStepListener: CustomJobListener,
) {
    private val logger: Logger = LoggerFactory.getLogger(QuerydslBatch::class.java)

    @Bean
    fun querydslBatchJob(): Job {
        return JobBuilder("querydslBatchJob", jobRepository)
            .start(querydslBatchStep())
            .listener(customJobListener)
            .build()
    }

    @Bean
    @JobScope
    fun querydslBatchStep(): Step {
        return StepBuilder("querydslBatchStep", jobRepository)
            .chunk<Department, Department>(chunkSize, transactionManager)
            .listener(customStepListener)
            .reader(querydslBatchReader())
            .writer(querydslBatchWriter())
            .build()
    }

    @Bean
    @StepScope
    fun querydslBatchReader(): QuerydslPagingItemReader<Department> {
        return QuerydslPagingItemReader(
            entityManagerFactory = entityManagerFactory,
            pageSize = chunkSize
        ) {
            it.selectFrom(QDepartment.department)
        }
    }

    @Bean
    @StepScope
    fun querydslBatchWriter(): ItemWriter<Department> {
        return ItemWriter { list: Chunk<out Department> ->
            list.forEach { logger.info(it.toString()) }
        }
    }

    companion object {
        const val chunkSize = 3
    }
}