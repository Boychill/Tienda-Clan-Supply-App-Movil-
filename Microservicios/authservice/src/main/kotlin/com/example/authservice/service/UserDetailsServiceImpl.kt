package com.example.authservice.service

import com.example.authservice.repository.UsuarioRepo
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val usuarios: UsuarioRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val u = usuarios.findByUsername(username).orElseThrow {
            UsernameNotFoundException("Usuario no existe")
        }

        val authorities: Collection<GrantedAuthority> =
            u.roles.map { role -> SimpleGrantedAuthority("ROLE_${role.name}") }

        return User(
            u.username,u.password,u.enabled,true,true,true,authorities
        )
    }
}