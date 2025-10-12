package com.example.catalogoservice.dominio

import jakarta.persistence.*

@Entity 
@Table(name = "categoria")
data class Categoria(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
  @Column(unique = true, nullable = false) val nombre: String,
  @Column(unique = true, nullable = false) val url: String
)
