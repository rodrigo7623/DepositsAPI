package cavapy.api.py.continental.repository;

import cavapy.api.py.continental.responses.BuscarResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BuscarResponseRepository extends CrudRepository<BuscarResponse, String> {

    @Query(value = "select id_movimientos_pk, " +
            "fecha, " +
            "monto, " +
            "comprobante, " +
            "tipo_documento, " +
            "numero_de_documento, " +
            "banco, " +
            "fecha_inicial, " +
            "fecha_fin, " +
            "ind_migracion, " +
            "rd.banco, " +
            "rd.numero_de_cuenta, " +
            "m.moneda, " +
            "descripcion " +
            "from movimientos m  " +
            "join movimientos_detalle md on m.id_movimientos_pk = md.id_movimientos_fk  " +
            "join referencia_detalle rd on md.id_referencia_detalle = rd.referencia  " +
            "where to_date(md.fecha, 'DD/MM/YYYY HH24:MI:SS') between to_date(:fechaInicial, 'YYYY-MM-DD') and to_date(:fechaFin, 'YYYY-MM-DD') " +
            "and m.hash = :selectedAccount ", nativeQuery = true)
    public List<BuscarResponse> getAllByFechaInicialAndFechaFin(@Param("fechaInicial") String fechaInicial,
                                                                @Param("fechaFin") String fechaFin,
                                                                @Param("selectedAccount") String selectedAccount);


    @Query(value = "select id_movimientos_pk, " +
            "fecha, " +
            "monto, " +
            "comprobante, " +
            "tipo_documento, " +
            "numero_de_documento, " +
            "banco, " +
            "fecha_inicial, " +
            "fecha_fin, " +
            "ind_migracion, " +
            "rd.banco, " +
            "rd.numero_de_cuenta, " +
            "m.moneda, " +
            "descripcion " +
            "from movimientos m  " +
            "join movimientos_detalle md on m.id_movimientos_pk = md.id_movimientos_fk  " +
            "LEFT join referencia_detalle rd on md.id_referencia_detalle = rd.referencia  " +
            "where comprobante = :comprobante ", nativeQuery = true)
    public BuscarResponse getBuscarResponseById(@Param("comprobante") String comprobante);



}
