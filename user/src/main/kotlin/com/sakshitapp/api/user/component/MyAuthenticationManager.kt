package com.sakshitapp.api.user.component

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MyAuthenticationManager : ReactiveAuthenticationManager {

    @Autowired
    private lateinit var firebaseAuth: FirebaseAuth

    @Autowired
    private lateinit var userService: UserService

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken: String = authentication.credentials.toString()
        try {
            val token = firebaseAuth.verifyIdToken(authToken)
            if (token != null) {
                val user = firebaseTokenToUserDto(token)
                return userService.get(user.uid).switchIfEmpty(userService.save(user))
                        .flatMap { Mono.just(UsernamePasswordAuthenticationToken(it.uid, token, it.getGrandAuthorities())) }
            }
        } catch (e: Exception) {
            return Mono.empty()
        }
        return Mono.empty()
    }

    private fun firebaseTokenToUserDto(decodedToken: FirebaseToken): User = User(
            uid = decodedToken.uid,
            name = decodedToken.name,
            photoURL = decodedToken.picture,
            email = decodedToken.email,
            phoneNumber = decodedToken.tenantId
    )
}