package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "notification")
data class Notification(
        @Id
        val uuid: String = UUID.randomUUID().toString(),
        val title: String? = null,
        val image: String? = null,
        val content: String? = null,
        val redirects: String? = null,
        val redirectType: RedirectType? = null,
        val typeId: String? = null,
        val type: NotificationType? = null,
        val messageId: String? = null
)