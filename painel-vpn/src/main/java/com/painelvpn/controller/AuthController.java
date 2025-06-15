package com.painelvpn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.painelvpn.dto.LoginRequest;
import com.painelvpn.service.AuthService;
import com.painelvpn.service.JwtService;
import com.painelvpn.model.Funcionario;
import com.painelvpn.repository.IFuncionarioRepository;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final IFuncionarioRepository funcionarioRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            AuthService authService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            IFuncionarioRepository funcionarioRepository) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.funcionarioRepository = funcionarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            logger.info("Tentativa de login para usuário: {}", request.getUsuario());
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsuario(),
                    request.getSenha()
                )
            );

            // Se chegou aqui, a autenticação foi bem-sucedida
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Busca o ID do funcionário
            Funcionario funcionario = funcionarioRepository.findByUsuario(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            
            // Gera o token incluindo o ID do funcionário
            String token = jwtService.generateToken(userDetails, funcionario.getIdFuncionario());
            
            // Registra o login bem-sucedido
            authService.handleSuccessfulLogin(request.getUsuario());

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (BadCredentialsException e) {
            // Registra a tentativa falha de login
            authService.handleFailedLogin(request.getUsuario());
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Usuário ou senha estão incorretos"));
        } catch (DisabledException e) {
            // Usuário bloqueado ou revogado
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
        } catch (AuthenticationException e) {
            // Captura outras exceções de autenticação
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Endpoint temporário para gerar senha criptografada
    @PostMapping("/encode-password")
    public ResponseEntity<String> encodePassword(@RequestBody String password) {
        String encoded = passwordEncoder.encode(password);
        logger.info("Senha criptografada gerada");
        return ResponseEntity.ok(encoded);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof String)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
        String funcionarioId = (String) authentication.getPrincipal();
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
            .orElse(null);
        if (funcionario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado");
        }
        // Por segurança, não envie a senha no JSON
        funcionario.setSenha("");
        return ResponseEntity.ok(funcionario);
    }
}

class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} 