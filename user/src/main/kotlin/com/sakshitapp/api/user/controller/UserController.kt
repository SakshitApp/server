package com.sakshitapp.api.user.controller

import com.sakshitapp.api.base.model.Response
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("")
    fun getUser(authentication: Authentication): Mono<Response<User>> =
        Mono.just(authentication.credentials as User)
            .map { Response(data = it) }

    @PostMapping("")
    fun updateUser(authentication: Authentication, @RequestBody user: User): Mono<Response<User>> =
        Mono.just(authentication.credentials as User)
            .flatMap { userService.save(it, user) }
            .map { Response(data = it) }

    @PatchMapping("")
    fun updateUser(
        authentication: Authentication,
        @RequestBody user: Map<*, *>
    ): Mono<Response<User>> = Mono.just(authentication.credentials as User)
        .flatMap { userService.save(it, user) }
        .map { Response(data = it) }

    @DeleteMapping("")
    fun delete(authentication: Authentication): Mono<Response<Void>> =
        Mono.just(authentication.credentials as User)
            .flatMap { userService.delete(it) }
            .map { Response(data = null) }
}