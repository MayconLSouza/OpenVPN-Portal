package com.painelvpn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.painelvpn.dto.SolicitacaoRedefinicaoSenhaRequest;
import com.painelvpn.dto.RedefinicaoSenhaRequest;
import com.painelvpn.service.RedefinicaoSenhaService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/senha")
public class RedefinicaoSenhaController {
    private static final Logger logger = LoggerFactory.getLogger(RedefinicaoSenhaController.class);
    private final RedefinicaoSenhaService redefinicaoSenhaService;

    public RedefinicaoSenhaController(RedefinicaoSenhaService redefinicaoSenhaService) {
        this.redefinicaoSenhaService = redefinicaoSenhaService;
    }

    @PostMapping("/solicitar-redefinicao")
    public ResponseEntity<String> solicitarRedefinicaoSenha(
            @RequestBody @Valid SolicitacaoRedefinicaoSenhaRequest request) {
        logger.info("Recebida solicitação de redefinição de senha");
        redefinicaoSenhaService.solicitarRedefinicaoSenha(request.getUsuario());
        return ResponseEntity.ok("Um e-mail com um link foi enviado para sua caixa de entrada");
    }

    @PostMapping("/redefinir")
    public ResponseEntity<String> redefinirSenha(@RequestBody @Valid RedefinicaoSenhaRequest request) {
        logger.info("Recebida solicitação de redefinição de senha com token");
        redefinicaoSenhaService.redefinirSenha(
            request.getToken(),
            request.getNovaSenha(),
            request.getConfirmacaoSenha()
        );
        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
} 