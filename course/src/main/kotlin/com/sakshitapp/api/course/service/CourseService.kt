package com.sakshitapp.api.course.service

import com.sakshitapp.api.base.model.*
import com.sakshitapp.api.course.repository.CartRepository
import com.sakshitapp.api.course.repository.CourseRepository
import com.sakshitapp.api.course.repository.SubscriptionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class CourseService {

    @Autowired
    private lateinit var courseRepository: CourseRepository

    @Autowired
    private lateinit var subscriptionRepository: SubscriptionRepository

    @Autowired
    private lateinit var cartRepository: CartRepository

    fun getHome(user: User): Mono<Home> {
        val c: Mono<List<Course>> = courseRepository.findAllByState(CourseState.ACTIVE.name)
            .collectList()
            .subscribeOn(Schedulers.parallel())
        val item: Mono<MutableList<Subscription>> =
            subscriptionRepository.findAllByUser(user.uid)
                .collectList()
                .subscribeOn(Schedulers.parallel())

        return Mono.zip(c, item)
            .flatMap { Mono.just(Home(subscribed = it.t2, courses = it.t1)) }
    }

    fun get(user: User, course: String): Mono<Subscription> =
        courseRepository.findById(course)
            .flatMap { course ->
                subscriptionRepository.findById(user.uid, course.uuid)
                    .switchIfEmpty(
                        subscriptionRepository.save(
                            Subscription(
                                user = user.uid,
                                courseId = course.uuid
                            )
                        )
                    ).map { it.copy(course = course) }
            }

    fun like(user: User, course: String): Mono<Subscription> =
        subscriptionRepository.findById(user.uid, course)
            .flatMap { subscription ->
                if (subscription.isLiked) {
                    courseRepository.findById(course)
                        .flatMap { Mono.just(subscription.copy(course = it)) }
                } else {
                    courseRepository.findById(course)
                        .flatMap { courseRepository.save(it.copy(likes = it.likes + 1)) }
                        .flatMap {
                            subscriptionRepository.save(
                                subscription.copy(
                                    isLiked = true
                                )
                            )
                        }
                }
            }

    fun addToCart(user: User, course: String): Mono<Cart> =
        cartRepository.findById(user.uid, course)
            .switchIfEmpty(
                courseRepository.findById(course)
                    .flatMap { cartRepository.save(Cart(user = user.uid, course = it)) }
            )

    fun review(user: User, course: String, review: String): Mono<Course> =
        courseRepository.findById(course)
            .flatMap {
                val list = arrayListOf<Review>()
                list.addAll(it.review)
                list.add(
                    Review(
                        user = user.uid,
                        userImage = user.photoURL,
                        userName = user.name,
                        review = review
                    )
                )
                courseRepository.save(it.copy(review = list))
            }

}