package com.example.springbatchstudy.common

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManagerFactory

open class QuerydslZeroPagingItemReader<T>(
    entityManagerFactory: EntityManagerFactory,
    transacted: Boolean = true,
    pageSize: Int,
    queryFunction: QueryBuilder<JPAQueryFactory, JPAQuery<T>>,
) : QuerydslPagingItemReader<T>(entityManagerFactory, transacted, pageSize, queryFunction) {

    override fun doReadPage() {
        clearIfTransacted()

        val query = createQuery()
            .offset(0)
            .limit(pageSize.toLong())

        initResults()
        fetchQuery(query)
    }

}