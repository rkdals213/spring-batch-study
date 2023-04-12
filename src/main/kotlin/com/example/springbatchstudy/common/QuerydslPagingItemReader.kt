package com.example.springbatchstudy.common

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.util.ClassUtils
import org.springframework.util.CollectionUtils
import java.util.concurrent.CopyOnWriteArrayList


open class QuerydslPagingItemReader<T>(
    private val entityManagerFactory: EntityManagerFactory,
    private var transacted: Boolean = true,
    pageSize: Int,
    private val queryFunction: QueryBuilder<JPAQueryFactory, JPAQuery<T>>,
) : AbstractPagingItemReader<T>() {

    private val jpaPropertyMap: Map<String, Any> = HashMap()
    private lateinit var entityManager: EntityManager

    init {
        name = ClassUtils.getShortName(QuerydslPagingItemReader::class.java)
        setPageSize(pageSize)
    }

    @Throws(Exception::class)
    override fun doOpen() {
        super.doOpen()
        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap)
    }

    override fun doReadPage() {
        clearIfTransacted()
        val query = createQuery()
            .offset((page * pageSize).toLong())
            .limit(pageSize.toLong())
        initResults()
        fetchQuery(query)
    }

    fun clearIfTransacted() {
        if (transacted) {
            entityManager.clear()
        }
    }

    fun createQuery(): JPAQuery<T> {
        val queryFactory = JPAQueryFactory(entityManager)
        return queryFunction.apply(queryFactory)
    }

    fun initResults() {
        if (CollectionUtils.isEmpty(results)) {
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }
    }

    fun fetchQuery(query: JPAQuery<T>) {
        if (!transacted) {
            val queryResult = query.fetch()
            for (entity in queryResult) {
                entityManager.detach(entity)
                results.add(entity)
            }
        } else {
            results.addAll(query.fetch())
        }
    }

    @Throws(Exception::class)
    override fun doClose() {
        entityManager.close()
        super.doClose()
    }
}

fun interface QueryBuilder<T, R> {
    fun apply(t: T): R
}