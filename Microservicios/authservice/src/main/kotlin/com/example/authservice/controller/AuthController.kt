package com.example.authservice.controller

import com.example.authservice.service.AuthService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val auth: AuthService) {

  data class RegisterReq(@field:NotBlank val username: String, @field:NotBlank val password: String)
  data class LoginReq(@field:NotBlank val username: String, @field:NotBlank val password: String)
  data class RefreshReq(@field:NotBlank val refreshToken: String)

  @PostMapping("/register")
  fun register(@Valid @RequestBody body: RegisterReq) = ResponseEntity.ok(auth.register(body.username, body.password))

  @PostMapping("/login")
  fun login(@Valid @RequestBody body: LoginReq) = ResponseEntity.ok(auth.login(body.username, body.password))

  @PostMapping("/refresh")
  fun refresh(@Valid @RequestBody body: RefreshReq) = ResponseEntity.ok(auth.refresh(body.refreshToken))

  @GetMapping("/me")
  fun me(@AuthenticationPrincipal jwt: Jwt?): ResponseEntity<Any> =
    ResponseEntity.ok(
      mapOf(
        "authenticated" to (jwt != null),
        "username" to (jwt?.subject ?: "anonymous"),
        "roles" to (jwt?.getClaim<List<String>>("roles") ?: emptyList<String>())
      )
    )
}
