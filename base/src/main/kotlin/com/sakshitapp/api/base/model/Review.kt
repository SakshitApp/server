package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "questions")
data class Review(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val user: String? = null,
    val userImage: String? = null,
    val userName: String? = null,
    val review: String? = null,
    val reply: List<Review> = emptyList()
)
