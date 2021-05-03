package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Pending
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface PendingRepository : ReactiveMongoRepository<Pending, String> {
    @Query("{ 'user': ?0 }")
    fun findAllByUser(user: String): Flux<Pending>
}