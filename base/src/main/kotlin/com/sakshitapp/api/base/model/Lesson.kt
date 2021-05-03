package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "lessons")
data class Lesson(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val title: String? = null,
    val image: String? = null,
    val description: String? = null,
    val content: String? = null,
    val review: List<Review> = emptyList(),
    val question: List<Question> = emptyList(),
    val passingQuestion: Int = 0,
    val likes: Long = 0
)
