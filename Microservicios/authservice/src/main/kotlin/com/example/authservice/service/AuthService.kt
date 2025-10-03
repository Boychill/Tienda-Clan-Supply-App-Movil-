package com.example.authservice.service
import com.example.authservice.dominio.Rol
import com.example.authservice.dominio.Usuario
import com.example.authservice.repository.RolRepo
import com.example.authservice.repository.UsuarioRepo
import com.example.authservice.seguridad.JwtService
import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
  private val users: UsuarioRepo,
  private val roles: RolRepo,
  private val encoder: PasswordEncoder,
  private val authManager: AuthenticationManager,
  private val jwt: JwtService
) {
  fun register(username: String, rawPassword: String, defaultRole: String = "CUSTOMER"): Tokens {
    require(users.findByUsername(username).isEmpty) { "Usuario ya existe" }
    val role = roles.findByName(defaultRole).orElseGet { roles.save(Rol(name = defaultRole)) }
    val user = users.save(Usuario(username = username, password = encoder.encode(rawPassword), roles = mutableSetOf(role)))
    return tokensFor(user.username, user.roles.map { it.name })
  }

  fun login(username: String, password: String): Tokens {
    authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
    val user = users.findByUsername(username).orElseThrow()
    return tokensFor(user.username, user.roles.map { it.name })
  }

  fun refresh(refreshToken: String): Tokens {
    val jws = jwt.parse(refreshToken)
    require(jwt.isRefreshToken(jws)) { "Token no es de tipo refresh" }
    val username = jws.body.subject
    val user = users.findByUsername(username).orElseThrow()
    return tokensFor(user.username, user.roles.map { it.name })
  }

  fun addRole(username: String, roleName: String) {
    val user = users.findByUsername(username).orElseThrow()
    val role = roles.findByName(roleName).orElseGet { roles.save(Rol(name = roleName)) }
    user.roles.add(role)
    users.save(user)
  }

  private fun tokensFor(username: String, roles: Collection<String>) =
    Tokens(
      accessToken = jwt.accessToken(username, roles),
      refreshToken = jwt.refreshToken(username),
      tokenType = "Bearer",
      expiresIn = 60L * 30
    )
}

data class Tokens(val accessToken: String, val refreshToken: String, val tokenType: String, val expiresIn: Long)
