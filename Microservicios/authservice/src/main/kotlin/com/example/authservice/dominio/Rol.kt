package com.example.authservice.dominio

import jakarta.persistence.*

@Entity 
@Table(name="rol")
data class Rol(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
  @Column(unique = true, nullable = false) val name: String
)