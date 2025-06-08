package com.painelvpn.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SenhaForteValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaForte {
    String message() default "A senha deve conter no mínimo 8 caracteres, incluindo pelo menos uma letra maiúscula, um número e um caractere especial (!@#$%&*-_+=.)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 