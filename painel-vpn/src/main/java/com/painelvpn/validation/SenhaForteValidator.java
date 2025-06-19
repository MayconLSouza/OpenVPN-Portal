package com.painelvpn.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaForteValidator implements ConstraintValidator<SenhaForte, String> {

    @Override
    public void initialize(SenhaForte constraintAnnotation) {
    }

    @Override
    public boolean isValid(String senha, ConstraintValidatorContext context) {
        if (senha == null) {
            return false;
        }

        // Verifica o tamanho mínimo
        boolean temTamanhoMinimo = senha.length() >= 8;

        // Verifica se tem pelo menos uma letra maiúscula
        boolean temMaiuscula = senha.matches(".*[A-Z].*");

        // Verifica se tem pelo menos um número
        boolean temNumero = senha.matches(".*[0-9].*");

        // Verifica se tem pelo menos um caractere especial
        boolean temCaracterEspecial = senha.matches(".*[!@#$%&*\\-_+=.].*");

        // Verifica se não tem espaços
        boolean temEspaco = senha.contains(" ");

        if (temEspaco || !temTamanhoMinimo || !temMaiuscula || !temNumero || !temCaracterEspecial) {
            if (context != null) {
                context.disableDefaultConstraintViolation();
                StringBuilder mensagem = new StringBuilder("A senha deve conter: ");
                
                if (temEspaco) {
                    mensagem.append("não deve conter espaços; ");
                }
                if (!temTamanhoMinimo) {
                    mensagem.append("no mínimo 8 caracteres; ");
                }
                if (!temMaiuscula) {
                    mensagem.append("pelo menos 1 letra maiúscula; ");
                }
                if (!temNumero) {
                    mensagem.append("pelo menos 1 número; ");
                }
                if (!temCaracterEspecial) {
                    mensagem.append("pelo menos 1 caractere especial (!@#$%&*-_+=.); ");
                }

                context.buildConstraintViolationWithTemplate(mensagem.toString().trim())
                       .addConstraintViolation();
            }
            return false;
        }

        return true;
    }
} 