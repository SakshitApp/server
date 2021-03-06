package com.sakshitapp.api.course

import com.sakshitapp.api.base.model.Course
import com.sakshitapp.api.base.model.Home
import com.sakshitapp.api.base.model.Response
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.course.service.EditCourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/course/edit")
class EditCourseController {

    @Autowired
    private lateinit var editCourseService: EditCourseService

    @GetMapping("")
    fun getCourses(authentication: Authentication): Mono<Response<Home>> =
            Mono.just(authentication.credentials as User)
                    .flatMap { editCourseService.get(it) }
                    .map { Response(data = it) }

    @GetMapping("/{id}")
    fun getCourse(
        authentication: Authentication,
        @PathVariable id: String
    ): Mono<Response<Course>> =
        Mono.just(authentication.credentials as User)
            .flatMap { editCourseService.getEditable(it, id) }
            .map { Response(data = it) }

    @GetMapping("/new")
    fun getNewCourse(authentication: Authentication): Mono<Response<Course>> =
        Mono.just(authentication.credentials as User)
            .flatMap { editCourseService.getEditable(it, null) }
            .map { Response(data = it) }


    @PostMapping("")
    fun saveCourse(
        authentication: Authentication,
        @RequestBody course: Course
    ): Mono<Response<Course>> =
        Mono.just(authentication.credentials as User)
            .flatMap { editCourseService.save(it, course) }
            .map { Response(data = it) }

    @DeleteMapping("")
    fun delete(
        authentication: Authentication,
        @RequestBody course: Course
    ): Mono<Response<Course>> =
        Mono.just(authentication.credentials as User)
            .flatMap { editCourseService.delete(it, course) }
            .map { Response(data = it) }


}