package com.example.authservice.seguridad
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey
import java.util.Base64

@Service
class JwtService(
  @Value("\${security.jwt.issuer}") private val issuer: String,
  @Value("\${security.jwt.access-token-minutes}") private val accessMinutes: Long,
  @Value("\${security.jwt.refresh-token-days}") private val refreshDays: Long,
  @Value("\${security.jwt.hmac-secret-base64}") secretB64: String
) {
  private val key: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretB64))
  private val parser: JwtParser = Jwts.parserBuilder().setSigningKey(key).requireIssuer(issuer).build()

  fun accessToken(username: String, roles: Collection<String>): String {
    val now = Instant.now(); val exp = now.plusSeconds(accessMinutes * 60)
    return Jwts.builder()
      .setSubject(username).setIssuer(issuer).setIssuedAt(Date.from(now)).setExpiration(Date.from(exp))
      .claim("roles", roles)
      .signWith(key, SignatureAlgorithm.HS256).compact()
  }

  fun refreshToken(username: String): String {
    val now = Instant.now(); val exp = now.plusSeconds(refreshDays * 24 * 3600)
    return Jwts.builder()
      .setSubject(username).setIssuer(issuer).setIssuedAt(Date.from(now)).setExpiration(Date.from(exp))
      .claim("typ", "refresh")
      .signWith(key, SignatureAlgorithm.HS256).compact()
  }

  fun parse(token: String): Jws<Claims> = parser.parseClaimsJws(token)
  fun isRefreshToken(jws: Jws<Claims>) = jws.body["typ"] == "refresh"
  fun key(): SecretKey = key
}
