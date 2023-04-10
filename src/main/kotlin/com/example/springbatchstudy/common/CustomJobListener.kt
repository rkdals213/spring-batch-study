package com.example.springbatchstudy.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.stereotype.Component

@Component
class CustomJobListener(
    private val logger: Logger = LoggerFactory.getLogger(CustomJobListener::class.java)
) : JobExecutionListener {
    override fun beforeJob(jobExecution: JobExecution) {
        logger.info("############################ job start")
        jobExecution.jobParameters
            .parameters
            .forEach { (key, value) ->
                logger.info("############################ parameter $key : $value")
            }
    }

    override fun afterJob(jobExecution: JobExecution) {
        logger.info("############################ job finish")
    }
}

@Component
class CustomStepExecutionListener(
    private val logger: Logger = LoggerFactory.getLogger(CustomStepExecutionListener::class.java)
) : StepExecutionListener {

    override fun beforeStep(stepExecution: StepExecution) {
        logger.info("############################ ${stepExecution.stepName} start")
        stepExecution.jobParameters
            .parameters
            .forEach { (key, value) ->
                logger.info("############################ parameter $key : $value")
            }
    }
    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        logger.info("############################ ${stepExecution.stepName} finish")
        return stepExecution.exitStatus
    }
}

@Component
class CustomChunkListener(
    private val logger: Logger = LoggerFactory.getLogger(CustomChunkListener::class.java)
) : ChunkListener {

    override fun beforeChunk(context: ChunkContext) {
        logger.info("############################ chunk start")
        logger.info("############################ readCount : ${context.stepContext.stepExecution.readCount}")
    }
    override fun afterChunk(context: ChunkContext) {
        logger.info("############################ chunk finish")
    }
}