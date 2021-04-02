package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "cart")
data class Cart(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val user: String? = null,
    val course: Course? = null
)