package com.sakshitapp.api.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

@Document(collection = "users")
data class User(
        @Id
        val uid: String,
        val password: String = UUID.randomUUID().toString(),
        var name: String? = null,
        var photoURL: String? = null,
        var email: String? = null,
        var phoneNumber: String? = null,
        var role: List<Role>? = null,
        var fcmToken: String? = null,
        var isActive: Boolean = true
) {
    fun getGrandAuthorities(): List<GrantedAuthority>? = role?.map { SimpleGrantedAuthority(it.name) }
}