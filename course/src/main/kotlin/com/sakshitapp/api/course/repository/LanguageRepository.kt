package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Language
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LanguageRepository : ReactiveMongoRepository<Language, String>