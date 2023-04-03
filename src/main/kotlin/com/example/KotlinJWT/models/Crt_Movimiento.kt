package com.example.KotlinJWT.models

import com.example.KotlinJWT.dto.Extracto
import jakarta.persistence.Column
import jakarta.persistence.ColumnResult
import jakarta.persistence.ConstructorResult
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.NamedNativeQuery
import jakarta.persistence.SqlResultSetMapping
import jakarta.persistence.Table

@NamedNativeQuery(
    name = "obtenerExtractos",
    query = "select " +
            "co.FECHA_EJECUCION as fecha, " +
            "co.ID_OPERACION as referencia, " +
            "cto.NOMBRE_CANAL as canal, " +
            "concat(ctm.ID_TIPO_MOVIMIENTO, ' - ', ctm.NOMBRE) as detalleTransaccion, " +
            "IF(SUBSTRING(cm.VALOR, 1,1) = '-', CONCAT(SUBSTRING(cm.VALOR, 1,1),'\$',SUBSTRING(cm.VALOR, 2)), CONCAT('+\$',SUBSTRING(cm.VALOR, 1))) as VALOR, " +
            "concat('\$',cm.SALDO_NUEVO) as saldo, " +
            "concat('\$',cm.SALDO_ANTERIOR) as saldoAnterior, " +
            "cc.CELULAR as numeroCuenta, " +
            "cp.NOMBRE as nombreProducto " +
            "from crt_movimiento cm " +
            "join crt_operacion co on cm.ID_OPERACION = co.ID_OPERACION " +
            "join crt_tipo_operacion cto on cto.ID_TIPO_OPERACION = co.ID_TIPO_OPERACION " +
            "join crt_tipo_movimiento ctm on cm.ID_TIPO_MOVIMIENTO = ctm.ID_TIPO_MOVIMIENTO " +
            "join crt_cuenta cc on cc.ID_CUENTA = cm.ID_CUENTA " +
            "join crt_producto cp on cp.ID_PRODUCTO = cc.ID_PRODUCTO " +
            "where cm.ID_CUENTA = :idCuenta " +
            "and co.FECHA_EJECUCION between :fechaInicio and :fechaFin " +
            "and not ctm.ID_TIPO_MOVIMIENTO like 'KC' " +
            "and not ctm.ID_TIPO_MOVIMIENTO like 'EB' " +
            "and not ctm.ID_TIPO_MOVIMIENTO like 'SP' " +
            "and not ctm.ID_TIPO_MOVIMIENTO like 'SA' " +
            "order by fecha, referencia asc",
    resultSetMapping = "extractoDto"
)
@SqlResultSetMapping(
    name = "extractoDto",
    classes = [ConstructorResult(
        targetClass = Extracto::class,
        columns = arrayOf(
            ColumnResult(name = "fecha"),
            ColumnResult(name = "detalleTransaccion"),
            ColumnResult(name = "canal"),
            ColumnResult(name = "referencia"),
            ColumnResult(name = "valor"),
            ColumnResult(name = "saldo"),
            ColumnResult(name = "saldoAnterior"),
            ColumnResult(name = "numeroCuenta"),
            ColumnResult(name = "nombreProducto"),
        )
    )]
)
@Entity(name = "crt_movimiento")
class Crt_Movimiento {
    @Id
    @Column(name = "ID_MOVIMIENTO")
    var idMovimiento = 0

    @Column(name = "ID_OPERACION")
    var idOperacion = 0

    @Column(name = "ID_TIPO_MOVIMIENTO")
    var idTipoMovimiento = ""

    @Column(name = "ID_CUENTA")
    var idCuenta = 0

    @Column(name = "ID_CAJA")
    var idCaja = 0

    @Column(name = "VALOR")
    var valor = 0

    @Column(name = "SALDO_ANTERIOR")
    var saldoAnterior = 0

    @Column(name = "SALDO_NUEVO")
    var saldoNuevo = 0

    @Column(name = "NOMBRE_PERSONA")
    var nombrePersona = ""

    @Column(name = "IMAGEN")
    var imagen = ""

    @Column(name = "NOMBRE_CUENTA")
    var nombreCuenta = ""

    @Column(name = "ID_PRODUCTO")
    var idProducto = ""

    @Column(name = "MOVIMIENTO_VISIBLE")
    var movimientoVisible = ""
}