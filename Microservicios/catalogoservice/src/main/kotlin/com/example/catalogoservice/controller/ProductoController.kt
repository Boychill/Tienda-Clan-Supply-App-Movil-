package com.example.catalogoservice.controller

import com.example.catalogoservice.ProductoService
import com.example.catalogoservice.dominio.Categoria
import com.example.catalogoservice.dominio.Producto
import com.example.catalogoservice.MapeadorProducto
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/productos")
class ControladorProducto(private val servicio: ServicioProducto) {

  // Listar todo o filtrar por q (nombre contiene) o por categoria (url)
  @GetMapping
  fun listar(
    @RequestParam(required = false) q: String?,
    @RequestParam(required = false, name = "categoria") urlCategoria: String?
  ): List<ProductoRes> =
    servicio.listar(q, urlCategoria).map(MapeadorProducto::aRes)

  @GetMapping("/{id}")
  fun obtener(@PathVariable id: Long) = MapeadorProducto.aRes(servicio.obtener(id))

  @GetMapping("/codigo/{codigo}")
  fun obtenerPorCodigo(@PathVariable codigo: String) =
    servicio.obtenerPorCodigo(codigo)?.let(MapeadorProducto::aRes)
      ?: throw NoSuchElementException("Producto con codigo $codigo no existe")

  @PostMapping
  fun crear(@Valid @RequestBody body: ProductoCrearReq) =
    ResponseEntity.ok(MapeadorProducto.aRes(servicio.crear(MapeadorProducto.aCrearCmd(body))))

  @PutMapping("/{id}")
  fun actualizar(@PathVariable id: Long, @Valid @RequestBody body: ProductoActualizarReq) =
    ResponseEntity.ok(MapeadorProducto.aRes(servicio.actualizar(id, MapeadorProducto.aActualizarCmd(body))))

  @DeleteMapping("/{id}")
  fun eliminar(@PathVariable id: Long) =
    servicio.eliminar(id).let { ResponseEntity.noContent().build<Void>() }
}