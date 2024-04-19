package cavapy.api.py.repository;

import cavapy.api.py.entity.MovimientosDetalle;
import cavapy.api.py.entity.MovimientosDetallePk;
import org.springframework.data.repository.CrudRepository;

public interface MovimientosDetalleRepository extends CrudRepository<MovimientosDetalle, String> {
}
