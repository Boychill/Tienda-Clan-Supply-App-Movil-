package com.example.authservice.repository

import com.example.authservice.dominio.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UsuarioRepo : JpaRepository<Usuario, Long> {
  fun findByUsername(username: String): Optional<Usuario>
}
