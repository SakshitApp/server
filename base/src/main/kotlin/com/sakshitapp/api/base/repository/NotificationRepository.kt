package com.sakshitapp.api.base.repository

import com.sakshitapp.api.base.model.Notification
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface NotificationRepository : ReactiveMongoRepository<Notification, String> {

    @Query("{ \$or: [ { type: \"ALL\" }, { typeId: ?0 } ] }")
    fun findByUser(user: String): Flux<Notification>
}