package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Subscription
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface SubscriptionRepository : ReactiveMongoRepository<Subscription, String> {
    @Query("{ 'user': ?0 }")
    fun findAllByUser(user: String): Flux<Subscription>

    @Query("{ 'user': ?0, 'courseId': ?1 }")
    fun findById(user: String, course: String): Mono<Subscription>
}