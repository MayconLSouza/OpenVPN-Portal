package com.painelvpn.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.painelvpn.model.Certificado;

@Repository
public interface ICertificadoRepository extends JpaRepository<Certificado, String> {
    List<Certificado> findByDataCriacaoBetween(LocalDate inicio, LocalDate fim);
    List<Certificado> findByIdContainingIgnoreCase(String identificador);
    boolean existsById(String id);
    
    @Query("SELECT c FROM Certificado c WHERE c.funcionario.idFuncionario = :funcionarioId")
    List<Certificado> findByFuncionarioId(String funcionarioId);
}
