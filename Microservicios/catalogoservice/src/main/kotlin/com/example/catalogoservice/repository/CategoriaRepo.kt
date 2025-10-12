package com.example.catalogoservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.example.catalogoservice.dominio.Categoria
import java.util.*

interface CategoriaRepo: JpaRepository<Categoria,Long>{
    fun findbyUrl(url:String):Optional<Categoria>
    fun existsByUrl(url: String): Boolean
}