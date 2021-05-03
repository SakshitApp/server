package com.sakshitapp.api.user.service

import com.sakshitapp.api.base.model.Role
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


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
        currentUser.apply {
            email = user.email
            name = user.name
            phoneNumber = user.phoneNumber
            photoURL = user.photoURL
        }
    )

    fun save(currentUser: User, user: Map<*, *>): Mono<User> = Mono.just(currentUser)
        .map { cUser ->
            User::class.memberProperties
                ?.filterIsInstance<KMutableProperty<*>>()
                ?.forEach {
                    var value = user[it.name]
                    if (value != null) {
                        it.isAccessible = true
                        if (it.name == "role") {
                            value = (value as? List<String>)?.map { Role.valueOf(it) }
                        }
                        it.setter.call(cUser, value)
                    }
                }
            cUser
        }
        .flatMap { userRepository.save(it) }

    fun delete(currentUser: User): Mono<Void> = Mono.just(currentUser)
        .map { it.apply { isActive = false } }
        .flatMap { userRepository.save(it) }
        .flatMap { Mono.empty() }
}