package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "transaction")
data class Transaction(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val user: String? = null,
    val courses: List<Course> = emptyList(),
    val state: TransactionState = TransactionState.STARTED,
    val createdOn: Long = System.currentTimeMillis(),
    val updatedOn: Long = System.currentTimeMillis()
)