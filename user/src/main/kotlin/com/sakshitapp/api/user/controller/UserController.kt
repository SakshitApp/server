package com.sakshitapp.api.user.controller

import com.fasterxml.jackson.databind.JsonNode
import com.sakshitapp.api.base.model.Response
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("")
    fun getUser(): Mono<Response<User>> = userService.getCurrentUser()
        .map { Response(data = it) }

    @PostMapping("")
    fun updateUser(@RequestBody user: User): Mono<Response<User>> = userService.save(user)
        .map { Response(data = it) }

    @PatchMapping("")
    fun updateUser(@RequestBody user: Map<String, JsonNode>): Mono<Response<User>> =
        userService.save(user)
            .map { Response(data = it) }

    @DeleteMapping("")
    fun delete(): Mono<Response<Void>> = userService.delete()
        .map { Response(data = null) }
}