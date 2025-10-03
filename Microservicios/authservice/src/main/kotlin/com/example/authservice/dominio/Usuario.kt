package com.example.authservice.dominio

import jakarta.persistence.*
import com.example.authservice.dominio.Rol


@Entity @Table(name="usuario")
data class Usuario(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
  @Column(unique = true, nullable = false) val username: String,
  @Column(nullable = false) val password: String,   // hash
  @Column(nullable = false) val enabled: Boolean = true,
  
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name="usuario_rol",
    joinColumns=[JoinColumn(name="usuario_id")],
    inverseJoinColumns=[JoinColumn(name="rol_id")]
  )
  val roles: MutableSet<Rol> = mutableSetOf()
)
