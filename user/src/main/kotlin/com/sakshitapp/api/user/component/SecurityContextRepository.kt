package com.sakshitapp.api.user.component

import org.apache.http.HttpHeaders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository : ServerSecurityContextRepository {

    @Autowired
    private lateinit var authenticationManager: MyAuthenticationManager

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> {
        if (exchange == null) return Mono.empty()
        val bearer = "Bearer "
        return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { it.startsWith(bearer) }
            .flatMap { Mono.just(it.substring(bearer.length)) }
            .flatMap { Mono.just(UsernamePasswordAuthenticationToken(it, it, null)) }
            .flatMap { authenticationManager.authenticate(it) }
            .map {
                SecurityContextHolder.setContext(SecurityContextImpl(it))
                SecurityContextHolder.getContext()
            }
    }
}