package com.sakshitapp.api.course

import com.sakshitapp.api.base.model.Config
import com.sakshitapp.api.base.model.Response
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.course.service.ConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ConfigController {

    @Autowired
    private lateinit var configService: ConfigService

    @GetMapping("/config")
    fun get(authentication: Authentication): Mono<Response<Config>> =
            Mono.just(authentication.credentials as User)
                    .flatMap { configService.get(it) }
                    .map { Response(data = it) }

    @PostMapping("/config")
    fun end(
            authentication: Authentication,
            @RequestBody data: Config
    ): Mono<Response<Config>> =
            Mono.just(authentication.credentials as User)
                    .flatMap { configService.save(it, data) }
                    .map { Response(data = it) }

    @GetMapping("/redeem")
    fun redeem(
            authentication: Authentication
    ): Mono<Response<String>> =
            Mono.just(authentication.credentials as User)
                    .flatMap { configService.redeem(it) }
                    .map { Response(data = it) }
}