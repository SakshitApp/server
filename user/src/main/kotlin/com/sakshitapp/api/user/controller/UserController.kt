package com.sakshitapp.api.user.controller

import com.fasterxml.jackson.databind.JsonNode
import com.sakshitapp.api.base.util.toSharedUser
import com.sakshitapp.api.user.service.UserService
import com.sakshitapp.shared.model.Response
import com.sakshitapp.shared.model.User
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
        .map { Response(data = toSharedUser(it)) }

    @PostMapping("")
    fun updateUser(@RequestBody user: User): Mono<Response<User>> = userService.save(user)
        .map { Response(data = toSharedUser(it)) }

    @PatchMapping("")
    fun updateUser(@RequestBody user: Map<String, JsonNode>): Mono<Response<User>> =
        userService.save(user)
            .map { Response(data = toSharedUser(it)) }

    @DeleteMapping("")
    fun delete(): Mono<Response<Void>> = userService.delete()
        .map { Response(data = null) }
}