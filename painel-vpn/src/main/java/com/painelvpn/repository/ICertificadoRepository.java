package com.painelvpn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.painelvpn.model.Certificado;

@Repository
public interface ICertificadoRepository extends JpaRepository<Certificado, String> {
    
}
