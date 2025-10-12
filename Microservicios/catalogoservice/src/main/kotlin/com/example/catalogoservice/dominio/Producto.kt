package com.example.catalogoservice.dominio

import jakarta.persistence.*
import com.example.catalogoservice.dominio.Categoria

@Entity
@Table(name = "producto")
data class Producto(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
  @Column(unique = true, nullable = false) val codigo: String,
  @Column(nullable = false) val nombre: String,
  @Column(columnDefinition = "text") val descripcion: String? = null,
  @Column(nullable = false) val precio: Int,
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categoria_id") 
  val categoria: Categoria? = null,
  @Column(nullable = false) val activo: Boolean = true
)