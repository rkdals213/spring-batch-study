package com.example.springbatchstudy.common

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.EntityTransaction
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.util.ClassUtils
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
        val tx = getTxOrNull()
        val query = createQuery()
            .offset((page * pageSize).toLong())
            .limit(pageSize.toLong())
        initResults()
        fetchQuery(query, tx)
    }

    protected open fun getTxOrNull(): EntityTransaction? {
        if (transacted) {
            val tx = entityManager.transaction
            tx.begin()
            entityManager.flush()
            entityManager.clear()
            return tx
        }
        return null
    }

    fun createQuery(): JPAQuery<T> {
        val queryFactory = JPAQueryFactory(entityManager)
        return queryFunction.apply(queryFactory)
    }

    fun initResults() {
        if (results.isNullOrEmpty()) {
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }
    }

    fun fetchQuery(query: JPAQuery<T>, tx: EntityTransaction?) {
        if (transacted) {
            results.addAll(query.fetch())
            tx?.commit()
        } else {
            val queryResult = query.fetch()
            for (entity in queryResult) {
                entityManager.detach(entity)
                results.add(entity)
            }
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