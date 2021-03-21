package com.sakshitapp.api.user.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
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
                .flatMap { user ->
                    if (user.isActive) Mono.just(user) else userRepository.save(
                        user.copy(
                            isActive = true
                        )
                    )
                }
        }
        return Mono.empty()
    }

    fun save(user: com.sakshitapp.shared.model.User): Mono<User> = getCurrentUser()
        .map {
            it.copy(
                email = user.email,
                name = user.name,
                phoneNumber = user.phoneNumber,
                photoURL = user.photoURL
            )
        }
        .flatMap { userRepository.save(it) }

    fun save(user: Map<String, JsonNode>): Mono<User> = getCurrentUser()
        .map {
            val mapper = ObjectMapper()
            val node = mapper.valueToTree<ObjectNode>(it).apply {
                user.forEach { entry ->
                    put(entry.key, entry.value)
                }
            }
            mapper.treeToValue(node, User::class.java)
        }
        .flatMap { userRepository.save(it) }

    fun delete(): Mono<Void> = getCurrentUser()
        .map { it.copy(isActive = false) }
        .flatMap { userRepository.save(it) }
        .flatMap { Mono.empty() }
}