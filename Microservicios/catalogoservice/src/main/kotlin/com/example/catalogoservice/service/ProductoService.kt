// service/ProductService.kt
package com.example.catalogoservice

import com.example.catalogoservice.dominio.Producto


interface ProductoService {
  fun listar(q: String?, categoria: String?): List<Producto>
  fun obtener(id: Long): Producto
  fun obtenerPorCodigo(codigo: String): Producto?
  fun crear(cmd: CrearProducto): Producto
  fun actualizar(id: Long, cmd: ActualizarProducto): Producto
  fun eliminar(id: Long)
  fun guardar(producto: Producto): Producto
}

data class CrearProducto(
  val codigo: String,
  val nombre: String,
  val descripcion: String?,
  val precio: Double,
  val categoria: String?,
  val activo: Boolean = true
)

data class ActualizarProducto(
  val nombre: String?,
  val descripcion: String?,
  val precio: Double?,
  val categoria: String?,
  val activo: Boolean?
)
