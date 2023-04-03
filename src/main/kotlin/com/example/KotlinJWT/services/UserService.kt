package com.example.KotlinJWT.services

import com.example.KotlinJWT.dto.Extracto
import com.example.KotlinJWT.dto.SendStatementRequest
import com.example.KotlinJWT.models.User
import com.example.KotlinJWT.repository.MovimientoRepository
import com.example.KotlinJWT.repository.UserRepository
import com.example.KotlinJWT.util.ReporteUtil
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val reporteUtil: ReporteUtil,
    private val movimientoRepository: MovimientoRepository
) {


    fun save(user: User): User {
        return this.userRepository.save(user)
    }

    fun findByEmail(email: String): User? {
        return this.userRepository.findByEmail(email)
    }

    fun getById(id: Int): User {
        return this.userRepository.getById(id)
    }

    fun obtenerExtractos(data: SendStatementRequest): List<String> {
        val extractosGenerados = mutableListOf<String>()
        val fechaInicial = data.fechaInicio.split("-")
        val fechaFinal = data.fechaFin.split("-")

        var mesInicial = fechaInicial[1].toInt()

        for(i in fechaInicial[0].toInt()..fechaFinal[0].toInt()){
            for (j in mesInicial..12){
                val fechaInicio = "$i-$j-1"
                val fechaFin = "$i-$j-" + reporteUtil.numeroDeDiasMes(j, i)

                val extractos: List<Extracto> =
                    movimientoRepository.obtenerExtractos(data.idCuenta, fechaInicio, fechaFin)

                if (!extractos.isEmpty()) {
                    var numeroProducto = extractos[0].numeroCuenta
                    var saldoInicial = if (extractos[0].saldoAnterior != null) extractos[0].saldoAnterior else "$0"
                    var saldoFinal = if (extractos[extractos.size-1].saldo != null) extractos[extractos.size-1].saldo else "$0"
                    var logo_powwi = "reportes/imagenes/LOGOS-07.png"
                    var logo_fogafin = "reportes/imagenes/Fogafin (1).png"
                    var footer = "reportes/imagenes/Footer.png"
                    var nombreProducto = extractos[0].nombreProducto

                    val parametrosReportes: HashMap<String, Any> = HashMap<String, Any>()

                    parametrosReportes.put(
                        "numeroOperacion", numeroProducto!!.substring(numeroProducto!!.length - 4, numeroProducto!!.length)
                    )
                    parametrosReportes.put("fechaInicio", fechaInicio)
                    parametrosReportes.put("fechaFin", fechaFin)
                    parametrosReportes.put("logo_powwi", logo_powwi)
                    parametrosReportes.put("logo_fogafin", logo_fogafin)
                    parametrosReportes.put("footer", footer)
                    parametrosReportes.put("saldoInicial", saldoInicial!!)
                    parametrosReportes.put("saldoFinal", saldoFinal!!)
                    parametrosReportes.put("nombreProducto", nombreProducto!!)

                    val jasperReport: JasperReport = reporteUtil.getJasperReport("/reportes/extracto.jrxml")!!

                    val camposReporte: HashMap<String, List<Extracto>> = HashMap<String, List<Extracto>>()

                    parametrosReportes.put("listaExtractos", JRBeanCollectionDataSource(extractos))
                    camposReporte["listaExtractos"] = extractos

                    reporteUtil.getPDF(extractos, parametrosReportes, jasperReport, data.idCuenta, j, i)

                    extractosGenerados.add("Extracto generado para las fechas $fechaInicio hasta $fechaFin")
                } else {
                    extractosGenerados.add("La cuenta no tiene movimientos en el rango de fechas $fechaInicio hasta $fechaFin")
                }

                if(i == fechaFinal[0].toInt() && j == fechaFinal[1].toInt()){
                    break
                }
            }
            mesInicial = 1
        }
        return extractosGenerados
    }
}