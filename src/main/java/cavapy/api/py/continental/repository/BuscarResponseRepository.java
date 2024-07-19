package cavapy.api.py.continental.repository;

import cavapy.api.py.continental.responses.BuscarResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BuscarResponseRepository extends CrudRepository<BuscarResponse, String> {

    @Query(value = "SELECT fecha, monto, comprobante, numero_de_documento, banco, ind_migracion, numero_de_cuenta, " +
            " moneda, descripcion, referencia " +
            " FROM 	v_movimientos_detalle_referencia " +
            " WHERE " +
            " to_date(fecha, 'DD/MM/YYYY HH24:MI:SS') between " +
            " to_date(:fechaInicial, 'YYYY-MM-DD') AND " +
            " to_date(:fechaFinal, 'YYYY-MM-DD') AND " +
            " hash = :selectedAccount", nativeQuery = true)
    List<BuscarResponse> getAllByFechaInicialAndFechaFin(@Param("fechaInicial") String fechaInicial,
                                                                @Param("fechaFinal") String fechaFinal,
                                                                @Param("selectedAccount") String selectedAccount);


    @Query(value = "select fecha, " +
            "monto, " +
            "comprobante, " +
            "numero_de_documento, " +
            "banco, " +
            "ind_migracion, " +
            "numero_de_cuenta, " +
            "moneda, " +
            "descripcion, " +
            "referencia " +
            "from v_movimientos_detalle_referencia  " +
            "where comprobante = :comprobante ", nativeQuery = true)
    BuscarResponse getBuscarResponseById(@Param("comprobante") String comprobante);



}
