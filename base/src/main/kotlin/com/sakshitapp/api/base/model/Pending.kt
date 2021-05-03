package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "pending")
data class Pending(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val user: String,
    val amount: Double = 0.0,
    val transaction: String
)
