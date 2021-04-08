package com.sakshitapp.api.course

import com.sakshitapp.api.base.model.Cart
import com.sakshitapp.api.base.model.RazorPayOrder
import com.sakshitapp.api.base.model.Response
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.course.service.CartService
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/cart")
class CartController {

    @Autowired
    private lateinit var cartService: CartService

    @GetMapping("")
    fun get(authentication: Authentication): Mono<Response<List<Cart>>> =
        Mono.just(authentication.credentials as User)
            .flatMap { cartService.getCart(it) }
            .map { Response(data = it) }

    @PutMapping("/{id}")
    fun add(authentication: Authentication, @PathVariable id: String): Mono<Response<List<Cart>>> =
        Mono.just(authentication.credentials as User)
            .flatMap { cartService.addToCart(it, id) }
            .map { Response(data = it) }

    @DeleteMapping("/{id}")
    fun remove(
        authentication: Authentication,
        @PathVariable id: String
    ): Mono<Response<List<Cart>>> =
        Mono.just(authentication.credentials as User)
            .flatMap { cartService.removeFromCart(it, id) }
            .map { Response(data = it) }

    @GetMapping("/transaction")
    fun start(authentication: Authentication): Mono<Response<JSONObject>> =
        Mono.just(authentication.credentials as User)
            .flatMap { cartService.startTransaction(it) }
            .map { Response(data = it) }

    @PostMapping("/transaction")
    fun end(
        authentication: Authentication,
        @RequestBody data: RazorPayOrder
    ): Mono<Response<List<Cart>>> =
        Mono.just(authentication.credentials as User)
            .flatMap { cartService.endTransaction(it, data) }
            .map { Response(data = it) }
}