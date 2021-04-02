package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Course
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CourseRepository : ReactiveMongoRepository<Course, String> {
    @Query("{ 'user': ?0, 'state': { \$ne: 'DELETED' } }")
    fun findAllByUser(user: String): Flux<Course>

    @Query("{ 'uuid': ?0, 'user': ?1, 'state': { \$ne: 'DELETED' }}")
    fun findByIdAndUser(uuid: String, user: String): Mono<Course>

    @Query("{ 'state': ?0 }")
    fun findAllByState(state: String): Flux<Course>
}