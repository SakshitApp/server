package com.sakshitapp.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @GetMapping
    fun start(): String = "Welcome to the Sakshit App!"

    @GetMapping("/hello")
    fun hello(): String = "Hello World!"
}