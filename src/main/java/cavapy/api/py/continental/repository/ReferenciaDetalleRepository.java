package cavapy.api.py.continental.repository;

import cavapy.api.py.continental.entity.ReferenciaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReferenciaDetalleRepository extends JpaRepository<ReferenciaDetalle, String> {
}
