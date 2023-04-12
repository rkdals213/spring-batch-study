package com.example.springbatchstudy.batchjobs.jpabatch

import com.example.springbatchstudy.common.CustomJobListener
import com.example.springbatchstudy.common.QuerydslPagingItemReader
import com.example.springbatchstudy.entity.QDepartment.department
import com.example.springbatchstudy.entity.QEmployee.employee
import com.example.springbatchstudy.entity.QEmployeeDepartment.employeeDepartment
import com.example.springbatchstudy.entity.QRecord.record
import com.querydsl.core.types.Projections
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
import java.time.LocalDateTime

@Configuration
class FindEmployeeLastOutTimeBatch(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val customJobListener: CustomJobListener,
    private val customStepListener: CustomJobListener,
) {
    private val logger: Logger = LoggerFactory.getLogger(FindEmployeeLastOutTimeBatch::class.java)

    @Bean
    fun findEmployeeLastOutTimeBatchJob(): Job {
        return JobBuilder("findEmployeeLastOutTimeBatchJob", jobRepository)
            .start(findEmployeeLastOutTimeBatchStep())
            .listener(customJobListener)
            .build()
    }

    @Bean
    @JobScope
    fun findEmployeeLastOutTimeBatchStep(): Step {
        return StepBuilder("findEmployeeLastOutTimeBatchStep", jobRepository)
            .chunk<FindEmployeeLastOutTime, FindEmployeeLastOutTime>(chunkSize, transactionManager)
            .listener(customStepListener)
            .reader(findEmployeeLastOutTimeBatchReader())
            .writer(findEmployeeLastOutTimeBatchWriter())
            .build()
    }

    @Bean
    @StepScope
    fun findEmployeeLastOutTimeBatchReader(): QuerydslPagingItemReader<FindEmployeeLastOutTime> {
        return QuerydslPagingItemReader(
            entityManagerFactory = entityManagerFactory,
            pageSize = chunkSize
        ) {
            it.select(
                Projections.constructor(
                    FindEmployeeLastOutTime::class.java,
                    department.id,
                    department.departmentName,
                    employee.id,
                    employee.firstName,
                    employee.lastName,
                    record.door,
                    record.region,
                    record.time.max()
                )
            )
                .from(department)
                .innerJoin(employeeDepartment).on(department.id.eq(employeeDepartment.departmentId))
                .innerJoin(employee).on(employeeDepartment.employeeId.eq(employee.id))
                .innerJoin(record).on(employee.id.eq(record.employeeId).and(record.recordSymbol.eq("O")))
                .groupBy(
                    department.id,
                    department.departmentName,
                    employee.id,
                    employee.firstName,
                    employee.lastName,
                    record.door,
                    record.region,
                )
                .orderBy(department.id.asc(), employee.id.asc(), record.door.asc())
        }
    }

    @Bean
    @StepScope
    fun findEmployeeLastOutTimeBatchWriter(): ItemWriter<FindEmployeeLastOutTime> {
        return ItemWriter { list: Chunk<out FindEmployeeLastOutTime> ->
            list.forEach { logger.info(it.toString()) }
        }
    }

    companion object {
        const val chunkSize = 500
    }
}

data class FindEmployeeLastOutTime(
    val departmentId: String,
    val departmentName: String,
    val employeeId: Long,
    val firstName: String,
    val lastName: String,
    val door: String,
    val region: String,
    val localDateTime: LocalDateTime,
)



