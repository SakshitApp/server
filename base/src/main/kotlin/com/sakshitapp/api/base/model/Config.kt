package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "config")
data class Config(
        @Id
        val userId: String,
        val user: User? = null,
        val isNotificationEnabled: Boolean = false,
        val redeem: Double? = null,
        val subscribedCourses: Int? = null,
        val completeCourses: Int? = null,
        val activeCourses: Int? = null,
        val inactiveCourses: Int? = null,
        val draftCourses: Int? = null
)
