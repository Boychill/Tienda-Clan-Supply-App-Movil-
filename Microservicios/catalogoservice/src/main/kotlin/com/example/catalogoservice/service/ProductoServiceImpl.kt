package com.example.catalogo.service

import com.example.catalogoservice.dominio.Producto
import com.example.catalogoservice.repository.CategoriaRepo
import com.example.catalogoservice.repository.ProductoRepo
import com.example.catalogoservice.ProductoService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
class ServicioProductoImpl(
  private val productos: ProductoRepo,
  private val categorias: CategoriaRepo
) : ProductoService {

  @Transactional(readOnly = true)
  override fun listar(q: String?, categoria: String?): List<Producto> =
    when {
      !categoria.isNullOrBlank() -> productos.findByCategoria_Url(categoria)
      !q.isNullOrBlank()            -> productos.findByNombre(q)
      else                          -> productos.findAll()
    }

  @Transactional(readOnly = true)
  override fun obtener(id: Long): Producto =
    productos.findById(id).orElseThrow { NoSuchElementException("Producto $id no existe") }

  @Transactional(readOnly = true)
  override fun obtenerPorCodigo(codigo: String): Producto? =
    productos.findByCodigo(codigo).orElse(null)

  @Transactional
  override fun crear(cmd: CrearProducto): Producto { 
    require(!productos.existsByCodigo(cmd.codigo)) { "Código ya existe" }

    val categoria = cmd.categoria?.let { url ->
      categorias.findbyUrl(url).orElseThrow { IllegalArgumentException("Categoría $url no existe") }
    }

    val entidad = Producto(
      codigo = cmd.codigo,
      nombre = cmd.nombre,
      descripcion = cmd.descripcion,
      precio = cmd.precio,
      categoria = Urlcategoria,
      activo = cmd.activo
    )
    return productos.save(entidad)
  }

  @Transactional
  override fun actualizar(id: Long, cmd: ActualizarProducto): Producto {
    val actual = productos.findById(id).orElseThrow { NoSuchElementException("Producto $id no existe") }

    val nuevaCategoria = cmd.urlCategoria?.let { url ->
      categorias.findbyUrl(url).orElseThrow { IllegalArgumentException("Categoría $url no existe") }
    } ?: actual.categoria

    val mod = actual.copy(
      nombre = cmd.nombre ?: actual.nombre,
      descripcion = cmd.descripcion ?: actual.descripcion,
      precio = cmd.precio ?: actual.precio,
      categoria = nuevaCategoria,
      activo = cmd.activo ?: actual.activo
    )
    return productos.save(mod)
  }

  @Transactional
  override fun eliminar(id: Long) {
    if (!productos.existsById(id)) throw NoSuchElementException("Producto $id no existe")
    productos.deleteById(id)
  }

  @Transactional
  override fun guardar(producto: Producto): Producto {
    // Validación simple para altas nuevas (evitar duplicado por 'codigo')
    if (producto.id == 0L && productos.existsByCodigo(producto.codigo)) {
      throw IllegalArgumentException("Código ya existe")
    }
    return productos.save(producto)
  }
}
