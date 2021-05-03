package com.sakshitapp.api.user.config

import com.sakshitapp.api.user.component.SecurityContextRepository
import com.sakshitapp.api.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var myAuthenticationManager: ReactiveAuthenticationManager

    @Autowired
    private lateinit var securityContextRepository: SecurityContextRepository

    @Bean
    fun userDetailService(): ReactiveUserDetailsService = ReactiveUserDetailsService { username ->
        userService.getCoreUserDetail(username)
    }

    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange()
            .pathMatchers("/").permitAll()
            .anyExchange().authenticated().and()
            .exceptionHandling()
            .authenticationEntryPoint { response, ex ->
                println("AuthenticationEntryPoint Exception: $ex")
                Mono.fromRunnable {
                    response.response.statusCode = HttpStatus.UNAUTHORIZED
                }
            }
            .accessDeniedHandler { response, ex ->
                println("AccessDeniedHandler Exception: $ex")
                Mono.fromRunnable {
                    response.response.statusCode = HttpStatus.FORBIDDEN
                }
            }.and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .authenticationManager(myAuthenticationManager)
                .securityContextRepository(securityContextRepository)
                .requestCache().requestCache(NoOpServerRequestCache.getInstance()).and()
                .build()
    }
}