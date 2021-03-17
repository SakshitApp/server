package com.sakshitapp.api.user.controller

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
    fun getUser(): Mono<User> = userService.getCurrentUser()

    @PostMapping("")
    fun updateUser(@RequestBody user: User): Mono<User> = userService.save(user)

    @DeleteMapping("")
    fun delete(): Mono<Void> = userService.delete()
}