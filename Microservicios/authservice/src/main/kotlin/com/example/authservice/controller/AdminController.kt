package com.example.authservice.controller

import com.example.authservice.service.AuthService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController(private val service: AuthService) {

  data class RoleReq(@field:NotBlank val username: String, @field:NotBlank val role: String)

  @PostMapping("/users/roles")
  @PreAuthorize("hasRole('ADMIN')")
  fun addRole(@RequestBody body: RoleReq): ResponseEntity<Void> {
    service.addRole(body.username, body.role)
    return ResponseEntity.noContent().build()
  }
}
