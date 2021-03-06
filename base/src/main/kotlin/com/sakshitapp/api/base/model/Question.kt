package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "questions")
data class Question(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val question: String? = null,
    val answers: List<String> = emptyList(),
    val correct: String = ""
)
