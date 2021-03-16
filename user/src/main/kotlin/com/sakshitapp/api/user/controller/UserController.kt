package com.sakshitapp.api.user.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/user")
class UserController {

    @GetMapping("/hello")
    fun hello(): Mono<String> = Mono.justOrEmpty("Hello World!")
}