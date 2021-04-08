package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Pending
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PendingRepository : ReactiveMongoRepository<Pending, String>