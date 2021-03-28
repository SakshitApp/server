package com.sakshitapp.api.course.service

import com.sakshitapp.api.base.model.*
import com.sakshitapp.api.course.repository.CategoryRepository
import com.sakshitapp.api.course.repository.CourseRepository
import com.sakshitapp.api.course.repository.LanguageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Service
class CourseService {

    @Autowired
    private lateinit var courseRepository: CourseRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var languageRepository: LanguageRepository

    fun get(user: User): Mono<List<Course>> =
        courseRepository.findAllByUser(user.uid)
            .collectList()

    fun getEditable(user: User, course: String?): Mono<Course> {
        val c: Mono<Course> = (if (course != null) courseRepository.findByIdAndUser(
            course,
            user.uid
        ) else courseRepository.save(Course(user = user.uid))).subscribeOn(Schedulers.parallel())
        val item: Mono<MutableList<Category>> =
            categoryRepository.findAll().subscribeOn(Schedulers.parallel()).collectList()
        val iteml: Mono<MutableList<Language>> =
            languageRepository.findAll().subscribeOn(Schedulers.parallel()).collectList()

        return Mono.zip(c, item, iteml)
            .flatMap { Mono.just(it.t1.copy(categoriesAll = it.t2, languagesAll = it.t3)) }
    }

    fun save(user: User, course: Course): Mono<Course> {
        val courses: Mono<Course> = courseRepository.save(
            course.copy(
                user = user.uid,
                categoriesAll = emptyList(),
                languagesAll = emptyList(),
                updatedOn = System.currentTimeMillis()
            )
        ).subscribeOn(Schedulers.parallel())
        val item: Mono<MutableList<Category>> =
            categoryRepository.saveAll(course.categories).subscribeOn(Schedulers.parallel())
                .collectList()
        val iteml: Mono<MutableList<Language>> =
            languageRepository.saveAll(course.languages).subscribeOn(Schedulers.parallel())
                .collectList()

        return Mono.zip(courses, item, iteml)
            .flatMap { Mono.just(course) }
    }

    fun delete(user: User, course: Course): Mono<Course> =
        courseRepository.save(
            course.copy(
                state = CourseState.DELETED,
                updatedOn = System.currentTimeMillis()
            )
        )


}