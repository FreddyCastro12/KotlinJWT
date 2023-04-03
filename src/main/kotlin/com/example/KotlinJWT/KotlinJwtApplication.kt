package com.example.KotlinJWT

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class KotlinJwtApplication

fun main(args: Array<String>) {
	runApplication<KotlinJwtApplication>(*args)
}
