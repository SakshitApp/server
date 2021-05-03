package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Transaction
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface TransactionRepository : ReactiveMongoRepository<Transaction, String> {

    @Query("{ 'order': ?0}")
    fun findByOrder(order: String): Mono<Transaction>
}