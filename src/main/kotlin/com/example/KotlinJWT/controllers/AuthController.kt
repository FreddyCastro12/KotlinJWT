package com.example.KotlinJWT.controllers

import com.example.KotlinJWT.dto.LoginDTO
import com.example.KotlinJWT.dto.Message
import com.example.KotlinJWT.dto.RegisterDTO
import com.example.KotlinJWT.dto.SendStatementRequest
import com.example.KotlinJWT.models.User
import com.example.KotlinJWT.services.UserService
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import java.util.*

@RestController
@RequestMapping("api")
class AuthController(private val userService: UserService) {

    @PostMapping("register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<User> {
        val user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password

        return ResponseEntity.ok(this.userService.save(user))
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found!"))

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body(Message("invalid password!"))
        }

        val issuer = listOf(user.id.toString(), user.name, user.email).joinToString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 1000))
            .signWith(SignatureAlgorithm.HS256, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        try {
            if (jwt == null) {
                return ResponseEntity.status(401).body(Message("unauthenticated"))
            }

            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body.issuer

            return ResponseEntity.ok(body)
        }catch (e: Exception){
            return ResponseEntity.status(401).body(Message("unauthenticated"))
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }

    @PostMapping("obtenerExtractos")
    fun obtenerExtractos(@RequestBody data: SendStatementRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(this.userService.obtenerExtractos(data))
    }

    @GetMapping("qrcode")
    fun generateQrCode(): String {
        val outputStream = ByteArrayOutputStream()
        val qrCodeWriter = QRCodeWriter()

        val bitMatrix: BitMatrix = qrCodeWriter.encode(
            ""
            , BarcodeFormat.QR_CODE
            , 200
            , 200)
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream)

        val s = Base64.getEncoder().encodeToString(outputStream.toByteArray())

        return s
    }
}