package com.example.KotlinJWT.repository

import com.example.KotlinJWT.dto.Extracto
import com.example.KotlinJWT.models.Crt_Movimiento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MovimientoRepository: JpaRepository<Crt_Movimiento, Int> {
    @Query(name = "obtenerExtractos", nativeQuery = true)
    fun obtenerExtractos(@Param("idCuenta") idCuenta: String, @Param("fechaInicio") fechaInicio: String, @Param("fechaFin") fechaFin: String): List<Extracto>
}