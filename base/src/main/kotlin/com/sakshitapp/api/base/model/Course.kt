package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "courses")
data class Course(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val user: String? = null,
    val userName: String = "Unknown",
    val title: String? = null,
    val image: String? = null,
    val summery: String? = null,
    val description: String? = null,
    val categories: List<Category> = emptyList(),
    val languages: List<Language> = emptyList(),
    val categoriesAll: List<Category> = emptyList(),
    val languagesAll: List<Language> = emptyList(),
    val price: Double = 0.0,
    val lessons: List<Lesson> = emptyList(),
    val likes: Long = 0,
    val subscriber: Long = 0,
    val review: List<Review> = emptyList(),
    val type: CourseType = CourseType.NO_INTRACTION,
    val state: CourseState = CourseState.DRAFT,
    val createdOn: Long = System.currentTimeMillis(),
    val updatedOn: Long = System.currentTimeMillis(),
)