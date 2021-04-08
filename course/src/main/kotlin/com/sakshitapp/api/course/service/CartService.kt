package com.sakshitapp.api.course.service

import com.razorpay.Order
import com.razorpay.RazorpayClient
import com.razorpay.RazorpayException
import com.sakshitapp.api.base.model.*
import com.sakshitapp.api.course.repository.*
import com.sakshitapp.api.course.util.Signature
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Service
class CartService {

    @Autowired
    private lateinit var courseRepository: CourseRepository

    @Autowired
    private lateinit var subscriptionRepository: SubscriptionRepository

    @Autowired
    private lateinit var cartRepository: CartRepository

    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @Autowired
    private lateinit var pendingRepository: PendingRepository

    fun getCart(user: User): Mono<List<Cart>> =
        cartRepository.findByUser(user.uid)
            .collectList()

    fun addToCart(user: User, course: String): Mono<List<Cart>> =
        cartRepository.findById(user.uid, course)
            .switchIfEmpty(
                courseRepository.findById(course)
                    .flatMap { cartRepository.save(Cart(user = user.uid, course = it)) }
            ).flatMap { getCart(user) }

    fun removeFromCart(user: User, course: String): Mono<List<Cart>> =
        cartRepository.findById(user.uid, course)
            .flatMap { cartRepository.delete(it) }
            .flatMap { getCart(user) }

    fun startTransaction(user: User): Mono<Map<String, Any?>> =
        getCart(user)
            .flatMap {
                transactionRepository.save(
                    Transaction(
                        user = user.uid,
                        courses = it.mapNotNull { it.course })
                )
            }
            .flatMap { transaction ->
                return@flatMap Mono.create {
                    try {
                        val ammount = transaction.courses.fold(
                            0.0,
                            { acc, next -> acc + (next.price ?: 0.0) })
                        if (ammount == 0.0) {
                            it.error(Exception("No item in cart"))
                        }

                        val orderRequest = JSONObject().apply {
                            put("amount", ammount)
                            put("currency", "INR")
                            put("receipt", transaction.uuid)
                        }
                        val key: String = System.getenv("RAZORPAY_KEY")
                        val secret: String = System.getenv("RAZORPAY_SECRET")
                        val razorpayClient = RazorpayClient(key, secret)
                        val order: Order = razorpayClient.Orders.create(orderRequest)
                        it.success(orderRequest.toMap())
                    } catch (e: RazorpayException) {
                        // Handle Exception
                        it.error(e)
                    }
                }
            }

    fun endTransaction(user: User, data: RazorPayOrder): Mono<List<Cart>> =
        Mono.create<List<Cart>> { sink ->
            val secret: String = System.getenv("RAZORPAY_SECRET")
            val signature = Signature.calculateRFC2104HMAC(
                data.razorpay_order_id + "|" + data.razorpay_payment_id,
                secret
            )
            if (signature == data.razorpay_signature) {
                val success = transactionRepository.findById(data.razorpay_order_id).block()
                success?.courses?.forEach {
                    val t1 = courseRepository.findById(it.uuid)
                        .flatMap { courseRepository.save(it.copy(subscriber = it.subscriber + 1)) }
                        .subscribeOn(Schedulers.parallel())
                    val t2 = pendingRepository.save(
                        Pending(
                            user = it.user ?: "",
                            amount = it.price,
                            transaction = success.uuid
                        )
                    )
                        .subscribeOn(Schedulers.parallel())
                    val t3 = subscriptionRepository.findById(user.uid, it.uuid)
                        .flatMap { subscriptionRepository.save(it.copy(transactionId = data.razorpay_order_id)) }
                        .switchIfEmpty(
                            subscriptionRepository.save(
                                Subscription(
                                    user = user.uid,
                                    courseId = it.uuid,
                                    transactionId = data.razorpay_order_id
                                )
                            )
                        ).subscribeOn(Schedulers.parallel())
                    Mono.zip(t1, t2, t3).block()
                }
                success?.let {
                    transactionRepository.save(
                        it.copy(
                            state = TransactionState.COMPLETED,
                            updatedOn = System.currentTimeMillis()
                        )
                    ).block()
                }
                getCart(user).flatMap { cartRepository.deleteAll(it) }.block()
                sink.success(emptyList())
            } else {
                sink.error(Exception("Invalid Signature"))
            }
        }.onErrorResume {
            transactionRepository.findById(data.razorpay_order_id)
                .flatMap {
                    transactionRepository.save(
                        it.copy(
                            state = TransactionState.FAILED,
                            updatedOn = System.currentTimeMillis()
                        )
                    )
                }
                .flatMap { getCart(user) }
        }

}