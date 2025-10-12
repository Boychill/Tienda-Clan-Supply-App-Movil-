package com.example.authservice.seguridad
import com.example.authservice.service.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain

@configuration
@EnableMethodSecurity
class SecurityConfig(
  private val uds: UserDetailsServiceImpl,
  private val jwt: JwtService
) {
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain =
    http
      .csrf { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .authorizeHttpRequests {
        it.requestMatchers("/actuator/health").permitAll()
        it.requestMatchers("/auth/**").permitAll()  // login, register, refresh
        it.requestMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")
        it.anyRequest().authenticated()
      }
      .oauth2ResourceServer { rs -> rs.jwt { } } // si expones endpoints protegidos aquí
      .build()

  @Bean fun authenticationManager(cfg: AuthenticationConfiguration): AuthenticationManager = cfg.authenticationManager
  @Bean fun jwtDecoder(): JwtDecoder = NimbusJwtDecoder.withSecretKey(jwt.key()).build()
}
