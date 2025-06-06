package com.painelvpn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.painelvpn.model.Funcionario;

@Repository
public interface IFuncionarioRepository extends JpaRepository<Funcionario, String> {
    Optional<Funcionario> findByUsuario(String usuario);
} 