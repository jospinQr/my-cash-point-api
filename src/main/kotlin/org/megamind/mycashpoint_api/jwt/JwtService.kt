package org.megamind.mycashpoint_api.jwt


import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    private val secret = "9D6FC88ACBDB8D26B4D0F4E78E9F89A7"
    private val key: SecretKey =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(Base64.getEncoder().encodeToString(secret.toByteArray())))

    fun generateToken(userId: Long, username: String, role: String, codeAgence: String?): String {
        val now = Date()
        val expiration = Date(now.time + 1000 * 60 * 600) // 10h

        val claims = mutableMapOf<String, Any>(
            "role" to role,
            "name" to username
        )
        if (codeAgence != null) {
            claims["agence_code"] = codeAgence
        }

        return Jwts.builder()
            .claims(claims)
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    // 🔹 Extrait le nom d'utilisateur
    fun extractUsername(token: String): String? = extractAllClaims(token)?.subject

    // 🔹 Extrait le rôle utilisateur
    fun extractRole(token: String): String? = extractAllClaims(token)?.get("role", String::class.java)
    fun extractCodeAgence(token: String): String? = extractAllClaims(token)?.get("agence_code", String::class.java)

    fun extractUserInfo(token: String): UserInfo? {
        val claims = extractAllClaims(token) ?: return null
        return UserInfo(
            username = claims.subject,
            role = claims.get("role", String::class.java),
            codeAgence = claims.get("agence_code", String::class.java)
        )
    }

    // 🔹 Méthode interne pour parser les claims
    private fun extractAllClaims(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            null
        }
    }

    // 🔹 Vérifie si le token est valide pour un utilisateur donné
    fun isTokenValid(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return extractedUsername == username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = extractAllClaims(token)?.expiration
        return expiration?.before(Date()) ?: true
    }
}

// 🔹 Data class pour encapsuler les informations du user
data class UserInfo(
    val username: String?,
    val role: String?,
    val codeAgence: String?,
)