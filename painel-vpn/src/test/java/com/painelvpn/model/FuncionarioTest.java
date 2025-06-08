package com.painelvpn.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class FuncionarioTest {
    
    private Validator validator;
    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        funcionario = new Funcionario();
        funcionario.setNome("Teste da Silva");
        funcionario.setEmail("teste@example.com");
    }

    @Test
    void testUsuarioValido() {
        funcionario.setUsuario("usuario123");
        funcionario.setSenha("Senha@123");
        
        Set<ConstraintViolation<Funcionario>> violations = validator.validate(funcionario);
        assertTrue(violations.isEmpty(), "Não deveria haver violações para um usuário válido");
    }

    @Test
    void testUsuarioMuitoCurto() {
        funcionario.setUsuario("ab");
        funcionario.setSenha("Senha@123");
        
        Set<ConstraintViolation<Funcionario>> violations = validator.validate(funcionario);
        assertFalse(violations.isEmpty(), "Deveria haver violação para usuário muito curto");
        
        boolean encontrouViolacao = violations.stream()
            .anyMatch(v -> v.getMessage().contains("O usuário deve ter entre 3 e 30 caracteres"));
        assertTrue(encontrouViolacao, "Deveria conter mensagem de erro sobre tamanho do usuário");
    }

    @Test
    void testUsuarioMuitoLongo() {
        funcionario.setUsuario("a".repeat(31)); // Cria uma string com 31 caracteres
        funcionario.setSenha("Senha@123");
        
        Set<ConstraintViolation<Funcionario>> violations = validator.validate(funcionario);
        assertFalse(violations.isEmpty(), "Deveria haver violação para usuário muito longo");
        
        boolean encontrouViolacao = violations.stream()
            .anyMatch(v -> v.getMessage().contains("O usuário deve ter entre 3 e 30 caracteres"));
        assertTrue(encontrouViolacao, "Deveria conter mensagem de erro sobre tamanho do usuário");
    }

    @Test
    void testSenhaSemMaiuscula() {
        funcionario.setUsuario("usuario123");
        funcionario.setSenha("senha@123");
        
        Set<ConstraintViolation<Funcionario>> violations = validator.validate(funcionario);
        assertFalse(violations.isEmpty(), "Deveria haver violação para senha sem letra maiúscula");
        
        boolean encontrouViolacao = violations.stream()
            .anyMatch(v -> v.getMessage().contains("pelo menos 1 letra maiúscula"));
        assertTrue(encontrouViolacao, "Deveria conter mensagem de erro sobre letra maiúscula");
    }

    @Test
    void testSenhaSemNumero() {
        funcionario.setUsuario("usuario123");
        funcionario.setSenha("Senha@abc");
        
        Set<ConstraintViolation<Funcionario>> violations = validator.validate(funcionario);
        assertFalse(violations.isEmpty(), "Deveria haver violação para senha sem número");
        
        boolean encontrouViolacao = violations.stream()
            .anyMatch(v -> v.getMessage().contains("pelo menos 1 número"));
        assertTrue(encontrouViolacao, "Deveria conter mensagem de erro sobre número");
    }

    @Test
    void testSenhaSemCaracterEspecial() {
        funcionario.setUsuario("usuario123");
        funcionario.setSenha("Senha123");
        
        Set<ConstraintViolation<Funcionario>> violations = validator.validate(funcionario);
        assertFalse(violations.isEmpty(), "Deveria haver violação para senha sem caractere especial");
        
        boolean encontrouViolacao = violations.stream()
            .anyMatch(v -> v.getMessage().contains("pelo menos 1 caractere especial"));
        assertTrue(encontrouViolacao, "Deveria conter mensagem de erro sobre caractere especial");
    }

    @Test
    void testSenhaMuitoCurta() {
        funcionario.setUsuario("usuario123");
        funcionario.setSenha("S@1");
        
        Set<ConstraintViolation<Funcionario>> violations = validator.validate(funcionario);
        assertFalse(violations.isEmpty(), "Deveria haver violação para senha muito curta");
        
        boolean encontrouViolacao = violations.stream()
            .anyMatch(v -> v.getMessage().contains("no mínimo 8 caracteres"));
        assertTrue(encontrouViolacao, "Deveria conter mensagem de erro sobre tamanho mínimo");
    }

    @Test
    void testMultiplasViolacoesNaSenha() {
        funcionario.setUsuario("usuario123");
        funcionario.setSenha("abc"); // Senha curta, sem maiúscula, sem número e sem caractere especial
        
        Set<ConstraintViolation<Funcionario>> violations = validator.validate(funcionario);
        assertFalse(violations.isEmpty(), "Deveria haver múltiplas violações");
        
        ConstraintViolation<Funcionario> senhaViolation = violations.stream()
            .filter(v -> v.getPropertyPath().toString().equals("senha"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(senhaViolation, "Deveria haver violação para o campo senha");
        String mensagem = senhaViolation.getMessage();
        
        assertTrue(mensagem.contains("no mínimo 8 caracteres"), "Deveria mencionar tamanho mínimo");
        assertTrue(mensagem.contains("pelo menos 1 letra maiúscula"), "Deveria mencionar letra maiúscula");
        assertTrue(mensagem.contains("pelo menos 1 número"), "Deveria mencionar número");
        assertTrue(mensagem.contains("pelo menos 1 caractere especial"), "Deveria mencionar caractere especial");
    }
} 