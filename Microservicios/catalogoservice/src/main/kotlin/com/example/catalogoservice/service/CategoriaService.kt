package com.example.catalogoservice.service

import com.example.catalogoservice.dominio.Categoria

interface CategoryService {
  fun listar(): List<Categoria>
  fun crear(nombre: String): Categoria
}