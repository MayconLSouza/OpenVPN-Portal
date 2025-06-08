package com.painelvpn.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.validation.ConstraintValidatorContext;

class SenhaForteValidatorTest {

    private SenhaForteValidator validator;
    
    @Mock
    private ConstraintValidatorContext context;
    
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new SenhaForteValidator();
        
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void testSenhaValida() {
        assertTrue(validator.isValid("Senha@123", context), "Senha válida deve passar na validação");
        assertTrue(validator.isValid("Abc123!@#", context), "Senha válida deve passar na validação");
        assertTrue(validator.isValid("MyP@ssw0rd", context), "Senha válida deve passar na validação");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "senha123", // sem maiúscula
        "SENHA123", // sem minúscula
        "Senhaaaa", // sem número
        "Senha123", // sem caractere especial
        "S@1", // muito curta
        "senha", // múltiplas violações
        "" // vazia
    })
    void testSenhasInvalidas(String senha) {
        assertFalse(validator.isValid(senha, context), 
            "Senha inválida não deve passar na validação: " + senha);
    }

    @Test
    void testSenhaNull() {
        assertFalse(validator.isValid(null, context), 
            "Senha null não deve passar na validação");
    }

    @Test
    void testCaracteresEspeciaisValidos() {
        assertTrue(validator.isValid("Senha!123", context), "! é válido");
        assertTrue(validator.isValid("Senha@123", context), "@ é válido");
        assertTrue(validator.isValid("Senha#123", context), "# é válido");
        assertTrue(validator.isValid("Senha$123", context), "$ é válido");
        assertTrue(validator.isValid("Senha%123", context), "% é válido");
        assertTrue(validator.isValid("Senha&123", context), "& é válido");
        assertTrue(validator.isValid("Senha*123", context), "* é válido");
        assertTrue(validator.isValid("Senha-123", context), "- é válido");
        assertTrue(validator.isValid("Senha_123", context), "_ é válido");
        assertTrue(validator.isValid("Senha+123", context), "+ é válido");
        assertTrue(validator.isValid("Senha=123", context), "= é válido");
        assertTrue(validator.isValid("Senha.123", context), ". é válido");
    }

    @Test
    void testSenhaComEspacos() {
        assertFalse(validator.isValid("Senha @123", context), 
            "Senha com espaços não deve ser válida");
    }

    @Test
    void testSenhaSoComCaracteresEspeciais() {
        assertFalse(validator.isValid("!@#$%&*-_+=.", context), 
            "Senha apenas com caracteres especiais não deve ser válida");
    }

    @Test
    void testSenhaSoComNumeros() {
        assertFalse(validator.isValid("12345678", context), 
            "Senha apenas com números não deve ser válida");
    }

    @Test
    void testSenhaSoComLetras() {
        assertFalse(validator.isValid("AbcdEfgh", context), 
            "Senha apenas com letras não deve ser válida");
    }
} 