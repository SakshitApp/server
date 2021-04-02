package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "subscription")
data class Subscription(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val user: String,
    val courseId: String,
    val course: Course? = null,
    val isLiked: Boolean = false,
    val progress: List<String> = emptyList(),
    val transactionId: String? = null
)