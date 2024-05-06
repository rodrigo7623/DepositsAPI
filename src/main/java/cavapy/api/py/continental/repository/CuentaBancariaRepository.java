package cavapy.api.py.continental.repository;

import cavapy.api.py.continental.entity.CuentaBancaria;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaBancariaRepository extends CrudRepository<CuentaBancaria, Integer> {

    Optional<List<CuentaBancaria>> findByEstado(String integer);


    Optional<CuentaBancaria> findByNumeroCuenta(String nroCuenta);
}
