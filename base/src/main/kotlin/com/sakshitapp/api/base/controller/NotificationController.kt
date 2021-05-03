package com.sakshitapp.api.base.controller

import com.sakshitapp.api.base.model.Notification
import com.sakshitapp.api.base.model.Response
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.base.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/notification")
class NotificationController {

    @Autowired
    private lateinit var notificationService: NotificationService

    @GetMapping("")
    fun get(authentication: Authentication): Mono<Response<List<Notification>>> =
            Mono.just(authentication.credentials as User)
                    .flatMap { notificationService.getNotification(it) }
                    .map { Response(data = it) }
}