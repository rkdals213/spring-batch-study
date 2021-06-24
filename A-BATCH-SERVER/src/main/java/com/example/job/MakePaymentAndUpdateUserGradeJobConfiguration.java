package com.example.job;

import com.example.PaymentDto;
import com.example.expression.Expression;
import com.example.options.QuerydslNoOffsetNumberOptions;
import com.example.order.entity.Order;
import com.example.readers.QuerydslNoOffsetPagingItemReader;
import com.example.user.Payment;
import com.example.user.QPayment;
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
import static com.example.user.QPayment.payment;

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
        return jobBuilderFactory.get("makePaymentAndUpdateUserGradeJob")
                .start(makePaymentStep())
                .next(updateUserGradeStep())
                .build();
    }

    @Bean
    public Step makePaymentStep() {
        return stepBuilderFactory.get("makePaymentStep")
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
                .sql("INSERT INTO PAYMENT(USER_ID, SHOP_ID, TOTAL_PAYMENT)\n" +
                        "VALUES (:userId, :shopId, :money)\n" +
                        "ON DUPLICATE KEY UPDATE TOTAL_PAYMENT = TOTAL_PAYMENT + :money")
                .beanMapped()
                .build();
    }

    @Bean
    public Step updateUserGradeStep() {
        return stepBuilderFactory.get("updateUserGradeStep")
                .<Payment, PaymentDto>chunk(chunkSize)
                .reader(readPayment())
                .processor(printPayment())
                .writer(updateUserGrade())
                .build();
    }

    @Bean
    public QuerydslNoOffsetPagingItemReader<Payment> readPayment() {
        QuerydslNoOffsetNumberOptions<Payment, Long> options = new QuerydslNoOffsetNumberOptions<>(payment.userId, Expression.ASC);
        return new QuerydslNoOffsetPagingItemReader(emf, chunkSize, options, jpaQueryFactory ->
                query.selectFrom(payment)
        );
    }

    @Bean
    public ItemProcessor<Payment, PaymentDto> printPayment() {
        return payment -> new PaymentDto(payment.getUserId(), payment.getShopId(), payment.getTotalPayment().longValue());
    }

    @Bean
    public JdbcBatchItemWriter<PaymentDto> updateUserGrade() {
        return new JdbcBatchItemWriterBuilder<PaymentDto>()
                .dataSource(dataSource)
                .sql("UPDATE USER_GRADE \n" +
                        "SET GRADE =\n" +
                        "        CASE\n" +
                        "            WHEN :money >= 3000000 AND :money < 5000000 THEN 'B'\n" +
                        "            WHEN :money >= 5000000 AND :money < 6000000 THEN 'A'\n" +
                        "            WHEN :money >= 6000000 AND :money < 7000000 THEN 'S'\n" +
                        "            ELSE 'C'\n" +
                        "            END\n" +
                        "WHERE USER_ID = :userId AND SHOP_ID = :shopId")
                .beanMapped()
                .build();
    }
}

