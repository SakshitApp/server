package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "languages")
data class Language(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    val name: String? = null
)