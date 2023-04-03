package com.example.KotlinJWT.util

import com.example.KotlinJWT.dto.Extracto
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*


@Service
class ReporteUtil {
    /**
     * Metodo que permite obtener el jasper de un archivo JRXML
     *
     * @param ruta Ruta del proyecto en la que se encuentra el jasper
     * @return Retorna el jasper
     */
    @Throws(Exception::class)
    fun getJasperReport(ruta: String?): JasperReport? {
        var recurso: InputStream? = null
        return try {
            recurso = javaClass.getResourceAsStream(ruta)
            JasperCompileManager.compileReport(recurso)
        } finally {
            recurso?.close()
        }
    }


    /**
     * Método que permite generar el PDF
     *
     * @param informacion        Contiene la información a reemplazar en el pdf
     * @param parametrosReportes Contiene los parámetros que recibe el pdf
     * @param jasperReport       Plantilla del reporte
     * @param nombrePDF          Nombre que tendrá el pdf
     * @return Retorna el dto que contiene el archivo generado en bits y el nombre del archivo
     */
    @Throws(Exception::class)
    fun getPDF(
        informacion: List<Extracto>?,
        parametrosReportes: Map<String, Any>,
        jasperReport: JasperReport?,
        idCuenta: String,
        mes: Int,
        anio: Int
    ) {
        // Se procede a generar el reporte en arreglo de bytes
        val source = JRBeanCollectionDataSource(informacion)
        val print = JasperFillManager.fillReport(jasperReport, parametrosReportes, source)
        val pdfBytes = JasperExportManager.exportReportToPdf(print)

        val directorio = File("/Cuenta-$idCuenta")
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                println("Directorio creado")
            } else {
                println("Error al crear directorio")
            }
        }

        var fileOutputStream: FileOutputStream? = null

        try {
            val file: File = File("/Cuenta-$idCuenta/extracto_$idCuenta-$anio-$mes.pdf")
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(pdfBytes)
        } finally {
            fileOutputStream?.close()
        }
    }


    /**
     * Devuelve el número de dias del mes (número) pasado por parámetro
     * Si es Febrero tiene en cuenta si este año es bisiesto o no
     * Empieza por 1
     * @param mes Mes que queremos saber el número de días
     * @return Número de días de ese mes
     */
    fun numeroDeDiasMes(mes: Int, anio: Int): Int {
        var numeroDias = -1
        when (mes) {
            1, 3, 5, 7, 8, 10, 12 -> numeroDias = 31
            4, 6, 9, 11 -> numeroDias = 30
            2 -> {
                val anioActual = Date()
                numeroDias = if (esBisiesto(1900 + anio)) {
                    29
                } else {
                    28
                }
            }
        }
        return numeroDias
    }

    /**
     * Indica si un año es bisiesto o no
     *
     * @param anio Año
     * @return True = es bisiesto
     */
    fun esBisiesto(anio: Int): Boolean {
        val calendar = GregorianCalendar()
        var esBisiesto = false
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true
        }
        return esBisiesto
    }
}