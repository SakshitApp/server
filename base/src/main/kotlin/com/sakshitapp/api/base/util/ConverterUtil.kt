package com.sakshitapp.api.base.util

import com.sakshitapp.shared.model.Role


fun toSharedUser(user: com.sakshitapp.api.base.model.User): com.sakshitapp.shared.model.User {
    return com.sakshitapp.shared.model.User(
        user.uid,
        user.name,
        user.photoURL,
        user.email,
        user.phoneNumber,
        user.role?.first()?.name?.let { it1 -> Role.valueOf(it1) })
}