package com.sakshitapp.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
