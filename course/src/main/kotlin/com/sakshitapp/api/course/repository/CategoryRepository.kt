package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Category
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : ReactiveMongoRepository<Category, String>