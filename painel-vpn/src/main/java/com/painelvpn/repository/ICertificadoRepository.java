package com.painelvpn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.painelvpn.model.Certificado;

public interface ICertificadoRepository extends JpaRepository<Certificado, String> {
    
}
