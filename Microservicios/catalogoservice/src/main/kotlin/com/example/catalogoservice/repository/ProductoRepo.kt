package com.example.catalogoservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.example.catalogoservice.dominio.Producto
import java.util.*

interface ProductoRepo : JpaRepository<Producto, Long> {
  fun findById(id: Long): Optional<Producto>
  fun findByNombre(q: String): List<Producto>
  fun findByCategoria_Url(url: String): List<Producto>
  fun findByCodigo(codigo: String): Optional<Producto>
  fun existsByCodigo(codigo: String): Boolean
  }