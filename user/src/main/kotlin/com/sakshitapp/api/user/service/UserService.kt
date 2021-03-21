package com.sakshitapp.api.user.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun getCoreUserDetail(uid: String?): Mono<org.springframework.security.core.userdetails.UserDetails> =
        userRepository.findById(
            uid
                ?: ""
        )
            .map {
                org.springframework.security.core.userdetails.User(
                    it.uid,
                    it.password,
                    it.getGrandAuthorities()
                )
            }

    fun get(uid: String): Mono<User> = userRepository.findById(uid)

    fun add(user: User): Mono<User> = userRepository.save(user)

    fun save(currentUser: User, user: User): Mono<User> = userRepository.save(
        currentUser.copy(
            email = user.email,
            name = user.name,
            phoneNumber = user.phoneNumber,
            photoURL = user.photoURL
        )
    )

    fun save(currentUser: User, user: Map<String, JsonNode>): Mono<User> = Mono.just(currentUser)
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

    fun delete(currentUser: User): Mono<Void> = Mono.just(currentUser)
        .map { it.copy(isActive = false) }
        .flatMap { userRepository.save(it) }
        .flatMap { Mono.empty() }
}