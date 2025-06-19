package com.painelvpn.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import com.painelvpn.model.Funcionario;
import com.painelvpn.model.TokenRedefinicaoSenha;
import com.painelvpn.repository.IFuncionarioRepository;
import com.painelvpn.repository.ITokenRedefinicaoSenhaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RedefinicaoSenhaService {
    private static final Logger logger = LoggerFactory.getLogger(RedefinicaoSenhaService.class);
    private static final int HORAS_VALIDADE_TOKEN = 1;

    private final IFuncionarioRepository funcionarioRepository;
    private final ITokenRedefinicaoSenhaRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public RedefinicaoSenhaService(
            IFuncionarioRepository funcionarioRepository,
            ITokenRedefinicaoSenhaRepository tokenRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void solicitarRedefinicaoSenha(String usuario) {
        // Sempre log a mesma mensagem, independente de encontrar ou não o usuário
        logger.info("Solicitação de redefinição de senha recebida");

        funcionarioRepository.findByUsuario(usuario).ifPresent(funcionario -> {
            String token = gerarToken();
            TokenRedefinicaoSenha tokenRedefinicao = new TokenRedefinicaoSenha(
                token,
                funcionario,
                LocalDateTime.now().plusHours(HORAS_VALIDADE_TOKEN)
            );

            tokenRepository.save(tokenRedefinicao);
            emailService.enviarEmailRedefinicaoSenha(funcionario.getEmail(), token);
        });

        // Sempre log a mesma mensagem, independente de ter enviado ou não o email
        logger.info("Processamento da solicitação de redefinição de senha concluído");
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha, String confirmacaoSenha) {
        if (!novaSenha.equals(confirmacaoSenha)) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem");
        }

        TokenRedefinicaoSenha tokenRedefinicao = tokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (!tokenRedefinicao.isValido()) {
            throw new IllegalArgumentException("Token expirado ou já utilizado");
        }

        Funcionario funcionario = tokenRedefinicao.getFuncionario();
        funcionario.setSenha(passwordEncoder.encode(novaSenha));
        funcionarioRepository.save(funcionario);

        tokenRedefinicao.setUsado(true);
        tokenRepository.save(tokenRedefinicao);

        logger.info("Senha redefinida com sucesso para o usuário: {}", funcionario.getUsuario());
    }

    private String gerarToken() {
        return UUID.randomUUID().toString();
    }
} 