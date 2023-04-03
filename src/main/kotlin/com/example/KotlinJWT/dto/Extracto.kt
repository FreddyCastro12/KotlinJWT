package com.example.KotlinJWT.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.json.JsonMapper
import java.io.Serializable
import java.util.Date

/**
 * Data clase para el objeto extracto
 */
data class Extracto(

        /**
         * Fecha en la que se realiza el extracto
         */
        val fecha: Date?,

        /**
         * Detalle de la transaccion extracto
         */
        val detalleTransaccion: String?,

        /**
         * Codigo del canal para el extracto
         */
        val canal: String?,

        /**
         * Referencia del extracto
         */
        val referencia: Int?,

        /**
         * Valor del extracto
         */
        val valor: String?,

        /**
         * Saldo disponible
         */
        val saldo: String?,

        val saldoAnterior: String?,

        val numeroCuenta: String?,

        val nombreProducto: String?
): Serializable {
    override fun toString(): String = JsonMapper
        .builder()
        .build()
        .findAndRegisterModules()
        .writeValueAsString(this)
}