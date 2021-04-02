package com.sakshitapp.api.base.model

data class Home(
    val subscribed: List<Subscription> = emptyList(),
    val courses: List<Course> = emptyList(),
)
