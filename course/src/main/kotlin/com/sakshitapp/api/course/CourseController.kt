package com.sakshitapp.api.course

import com.sakshitapp.api.base.model.*
import com.sakshitapp.api.course.service.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/course")
class CourseController {
    @Autowired
    private lateinit var courseService: CourseService

    @GetMapping("")
    fun getCourses(authentication: Authentication): Mono<Response<Home>> =
        Mono.just(authentication.credentials as User)
            .flatMap { courseService.getHome(it) }
            .map { Response(data = it) }

    @GetMapping("/{id}")
    fun getCourse(
        authentication: Authentication,
        @PathVariable id: String
    ): Mono<Response<Subscription>> =
        Mono.just(authentication.credentials as User)
            .flatMap { courseService.get(it, id) }
            .map { Response(data = it) }

    @PostMapping("/like/{id}")
    fun like(
        authentication: Authentication,
        @PathVariable id: String
    ): Mono<Response<Subscription>> =
        Mono.just(authentication.credentials as User)
            .flatMap { courseService.like(it, id) }
            .map { Response(data = it) }

    @PostMapping("/review/{id}")
    fun review(
        authentication: Authentication,
        @PathVariable id: String,
        @RequestBody map: HashMap<String, *>
    ): Mono<Response<Course>> =
        Mono.just(authentication.credentials as User)
            .flatMap {
                val review = map["review"] as? String
                if (review != null) {
                    courseService.review(it, id, review)
                } else {
                    Mono.error(Exception("Invalid Params"))
                }
            }
            .map { Response(data = it) }
}