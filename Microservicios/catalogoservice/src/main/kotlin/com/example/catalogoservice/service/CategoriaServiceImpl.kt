package com.example.catalogoservice.service

import com.example.catalogoservice.dominio.Categoria
import com.example.catalogoservice.repository.CategoriaRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.Normalizer

@Service
class CategoriaServiceImpl(
  private val categorias: CategoriaRepo
) : CategoriaService {

  @Transactional(readOnly = true)
  override fun listar(): List<Categoria> = categorias.findAll()

  @Transactional(readOnly = true)
  override fun obtenerPorUrl(url: String): Categoria? =
    categorias.findbyUrl(url).orElse(null)

  @Transactional
  override fun crear(nombre: String): Categoria {
    val base = generarUrl(nombre)
    val url = asegurarUrlUnica(base) { s -> categorias.existsByUrl(s) }
    return categorias.save(Categoria(nombre = nombre, url = url))
  }

  @Transactional
  override fun guardar(categoria: Categoria): Categoria {
    // Si viene sin 'url', se genera; si viene con 'url', se respeta.
    val urlFinal = if (categoria.url.isBlank()) {
      val base = generarUrl(categoria.nombre)
      asegurarUrlUnica(base) { s -> categorias.existsByUrl(s) }
    } else categoria.url

    val entidad = if (categoria.url == urlFinal) categoria else categoria.copy(url = urlFinal)
    return categorias.save(entidad)
  }

  // --- helpers privados ---

  private fun generarUrl(input: String): String {
    val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
    val sinTildes = normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    return sinTildes
      .lowercase()
      .replace("[^a-z0-9]+".toRegex(), "-")
      .trim('-')
      .ifBlank { "n-a" }
  }

  private fun asegurarUrlUnica(base: String, existe: (String) -> Boolean): String {
    var candidato = base
    var i = 2
    while (existe(candidato)) {
      candidato = "$base-$i"
      i++
    }
    return candidato
  }
}
