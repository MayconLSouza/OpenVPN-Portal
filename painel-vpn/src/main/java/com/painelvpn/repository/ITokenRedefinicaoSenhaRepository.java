package com.painelvpn.repository;

import com.painelvpn.model.TokenRedefinicaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ITokenRedefinicaoSenhaRepository extends JpaRepository<TokenRedefinicaoSenha, Long> {
    Optional<TokenRedefinicaoSenha> findByToken(String token);
} 