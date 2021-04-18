package com.sakshitapp.api.course.service

import com.sakshitapp.api.base.model.Config
import com.sakshitapp.api.base.model.CourseState
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.course.repository.ConfigRepository
import com.sakshitapp.api.course.repository.CourseRepository
import com.sakshitapp.api.course.repository.PendingRepository
import com.sakshitapp.api.course.repository.SubscriptionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class ConfigService {

    @Autowired
    private lateinit var configRepository: ConfigRepository

    @Autowired
    private lateinit var pendingRepository: PendingRepository

    @Autowired
    private lateinit var courseRepository: CourseRepository

    @Autowired
    private lateinit var subscriptionRepository: SubscriptionRepository

    fun get(user: User): Mono<Config> =
            configRepository.findById(user.uid)
                    .switchIfEmpty {
                        configRepository.save(Config(user.uid))
                    }
                    .flatMap { config ->
                        val pending = pendingRepository.findAllByUser(user.uid).subscribeOn(Schedulers.parallel()).collectList()
                        val courses = courseRepository.findAllByUser(user.uid).subscribeOn(Schedulers.parallel()).collectList()
                        val subs = subscriptionRepository.findAllByUser(user.uid)
                                .flatMap { subs ->
                                    courseRepository.findById(subs.courseId)
                                            .map { subs.copy(course = it) }
                                }.subscribeOn(Schedulers.parallel()).collectList()
                        Mono.zip(pending, courses, subs)
                                .flatMap {
                                    Mono.just(config.copy(
                                            user = user,
                                            redeem = it.t1.fold(0.0, { acc, next -> acc + next.amount }),
                                            subscribedCourses = it.t3.filter { it.transactionId != null }.size
                                                    ?: 0,
                                            completeCourses = it.t3.filter { it.transactionId != null && it.progress.size == it.course?.lessons?.size }.size
                                                    ?: 0,
                                            activeCourses = it.t2.filter { it.state == CourseState.ACTIVE }.size
                                                    ?: 0,
                                            inactiveCourses = it.t2.filter { it.state == CourseState.INACTIVE }.size
                                                    ?: 0,
                                            draftCourses = it.t2.filter { it.state == CourseState.DRAFT }.size
                                                    ?: 0
                                    ))
                                }
                    }

    fun save(user: User, config: Config): Mono<Config> =
            configRepository.save(config.copy(userId = user.uid, user = null))

    fun redeem(user: User): Mono<String> =
            pendingRepository.findAllByUser(user.uid)
                    .flatMap { pendingRepository.delete(it) }
                    .collectList()
                    .flatMap { Mono.just("Deleted") }
                    .switchIfEmpty { Mono.just("Deleted") }
}