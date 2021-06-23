package com.example.job;

import com.example.support.CreateDateJobParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobParameterExtendsConfiguration {

    private final CreateDateJobParameter jobParameter; // (1)
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final String JOB_NAME = "simpleJobWithJobParameter";

    @Bean(JOB_NAME + "jobParameter")
    @JobScope // (2)
    public CreateDateJobParameter jobParameter() {
        return new CreateDateJobParameter();
    }

    @Bean
    public Job simpleJobWithJobParameter() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(simpleJobWithJobParameterStep1())
                .build();
    }

    @Bean
    @JobScope
    public Step simpleJobWithJobParameterStep1() {
        Map<String, Object> params = new HashMap<>();
        params.put("createDate", jobParameter.getCreateDate()); // (4)

        return stepBuilderFactory.get(JOB_NAME + "_step")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.info(">>>>>>>>>>> createDate={}", jobParameter.getCreateDate());
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}


