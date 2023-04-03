package com.example.KotlinJWT.repository

import com.example.KotlinJWT.dto.Extracto
import com.example.KotlinJWT.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository: JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
}