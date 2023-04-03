package com.example.KotlinJWT.dto

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

/**
 * Data clase para el objeto consultar envio extracto
 */
class SendStatementRequest {
    /**
     * Id de la cuenta que realiza la peticio
     */
    val idCuenta: String = ""

    /**
     * Mes de consulta
     */
    val fechaInicio: String = ""

    val fechaFin: String = ""
}
