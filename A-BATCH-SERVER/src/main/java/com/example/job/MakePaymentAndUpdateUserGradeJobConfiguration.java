package com.example.job;

import com.example.PaymentDto;
import com.example.expression.Expression;
import com.example.options.QuerydslNoOffsetNumberOptions;
import com.example.order.entity.Order;
import com.example.readers.QuerydslNoOffsetPagingItemReader;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static com.example.order.entity.QOrder.order;

@Slf4j
@Configuration
public class MakePaymentAndUpdateUserGradeJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final JPAQueryFactory query;
    private final DataSource dataSource; // DataSource DI

    private final int chunkSize = 10;

    public MakePaymentAndUpdateUserGradeJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory emf, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.emf = emf;
        this.query = new JPAQueryFactory(emf.createEntityManager());
        this.dataSource = dataSource;
    }

    @Bean
    public Job makePaymentAndUpdateUserGradeJob() {
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .start(makePaymentStep())
                .build();
    }

    @Bean
    public Step makePaymentStep() {
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<Order, PaymentDto>chunk(chunkSize)
                .reader(readOrder())
                .processor(orderToPayment())
                .writer(writePayment())
                .build();
    }

    @Bean
    public QuerydslNoOffsetPagingItemReader<Order> readOrder() {
        QuerydslNoOffsetNumberOptions<Order, Long> options = new QuerydslNoOffsetNumberOptions<>(order.id, Expression.ASC);
        return new QuerydslNoOffsetPagingItemReader(emf, chunkSize, options, jpaQueryFactory ->
                query.selectFrom(order)
        );
    }

    @Bean
    public ItemProcessor<Order, PaymentDto> orderToPayment() {
        return order -> new PaymentDto(order.getUserId(), order.getEntries().get(0).getProduct().getShopId(), order.calculateTotalPrice().longValue());
    }

    @Bean
    public JdbcBatchItemWriter<PaymentDto> writePayment() {
        return new JdbcBatchItemWriterBuilder<PaymentDto>()
                .dataSource(dataSource)
                .sql("insert into PAYMENT(USER_ID, SHOP_ID, TOTAL_PAYMENT) values (:userId, :shopId, :money) ON DUPLICATE KEY UPDATE TOTAL_PAYMENT = TOTAL_PAYMENT + :money")
                .beanMapped()
                .build();
    }
}

