package cavapy.api.py.continental.service;

import cavapy.api.py.continental.entity.ReferenciaDetalle;
import cavapy.api.py.continental.repository.ReferenciaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositsService {

    @Autowired
    private ReferenciaDetalleRepository referenciaDetalleRepository;

    @Transactional
    public void UpdateReferenciaDetalle(ReferenciaDetalle referenciaDetalle) {
        referenciaDetalleRepository.save(referenciaDetalle);
        referenciaDetalleRepository.flush();
    }
}
