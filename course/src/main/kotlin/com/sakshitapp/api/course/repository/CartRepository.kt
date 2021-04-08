package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Cart
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CartRepository : ReactiveMongoRepository<Cart, String> {
    @Query("{ 'user': ?0, 'course.uuid': ?1 }")
    fun findById(user: String, course: String): Mono<Cart>

    @Query("{ 'user': ?0}")
    fun findByUser(user: String): Flux<Cart>
}