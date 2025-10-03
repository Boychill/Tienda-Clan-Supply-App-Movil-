package com.example.authservice.repository

import com.example.authservice.dominio.Rol
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RolRepo : JpaRepository<Rol, Long> {
  fun findByName(name: String): Optional<Rol>
}
