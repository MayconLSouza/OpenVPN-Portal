package com.painelvpn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.painelvpn.model.Funcionario;

@Repository
public interface IFuncionarioRepository extends JpaRepository<Funcionario, String> {
    
} 