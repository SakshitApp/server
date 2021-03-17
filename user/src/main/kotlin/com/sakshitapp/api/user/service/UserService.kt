package com.sakshitapp.api.user.service

import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.user.repository.UserRepository
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class UserService {

    private lateinit var userRepository: UserRepository

    fun getCoreUserDetail(uid: String?): Mono<org.springframework.security.core.userdetails.UserDetails> = userRepository.findById(uid
            ?: "")
            .map { org.springframework.security.core.userdetails.User(it.uid, it.password, it.getGrandAuthorities()) }

    fun get(uid: String): Mono<User> = userRepository.findById(uid)

    fun getCurrentUser(): Mono<User> {
        val securityContext: SecurityContext = SecurityContextHolder.getContext()
        (securityContext?.authentication?.principal as? String)?.let {
            return userRepository.findById(it)
                    .flatMap { user -> if (user.isActive) Mono.just(user) else save(user.copy(isActive = true)) }
        }
        return Mono.empty()
    }

    fun save(user: User): Mono<User> = userRepository.save(user)

    fun delete(): Mono<Void> = getCurrentUser()
            .map { it.copy(isActive = false) }
            .flatMap { save(it) }
            .flatMap { Mono.empty() }
}