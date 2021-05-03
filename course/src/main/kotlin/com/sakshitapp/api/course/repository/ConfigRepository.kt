package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Config
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ConfigRepository : ReactiveMongoRepository<Config, String>