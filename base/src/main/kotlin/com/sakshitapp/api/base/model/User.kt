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
        val name: String? = null,
        val photoURL: String? = null,
        val email: String? = null,
        val phoneNumber: String? = null,
        val role: List<Role>? = null,
        val fcmToken: String? = null,
        val isActive: Boolean = true
) {
    fun getGrandAuthorities(): List<GrantedAuthority>? = role?.map { SimpleGrantedAuthority(it.name) }
}