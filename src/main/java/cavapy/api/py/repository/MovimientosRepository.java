package cavapy.api.py.repository;

import cavapy.api.py.entity.Movimientos;
import org.springframework.data.repository.CrudRepository;

public interface MovimientosRepository extends CrudRepository<Movimientos, Integer> {
}
