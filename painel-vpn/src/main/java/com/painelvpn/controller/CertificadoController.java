package com.painelvpn.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.painelvpn.model.Certificado;
import com.painelvpn.model.Funcionario;
import com.painelvpn.service.CertificadoService;
import com.painelvpn.service.FuncionarioService;
import com.painelvpn.dto.CertificadoDTO;

@RestController
@RequestMapping("/api/certificados")
public class CertificadoController {

    private final CertificadoService certificadoService;
    private final FuncionarioService funcionarioService;

    public CertificadoController(CertificadoService certificadoService, FuncionarioService funcionarioService) {
        this.certificadoService = certificadoService;
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<Certificado> criarCertificado(@AuthenticationPrincipal String funcionarioId) {
        try {
            Funcionario funcionario = funcionarioService.buscarPorId(funcionarioId);
            Certificado certificado = certificadoService.criarCertificado(funcionario);
            return ResponseEntity.ok(certificado);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificadoDTO>> listarCertificados(
            @AuthenticationPrincipal String funcionarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String identificador) {
        
        List<Certificado> certificados;
        
        if (dataInicio != null && dataFim != null) {
            certificados = certificadoService.buscarPorPeriodo(dataInicio, dataFim);
        } else if (identificador != null) {
            certificados = certificadoService.buscarPorIdentificador(identificador);
        } else {
            certificados = certificadoService.buscarTodosCertificados();
        }
        
        List<CertificadoDTO> certificadosDTO = certificados.stream()
            .map(CertificadoDTO::new)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(certificadosDTO);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadCertificado(
            @PathVariable String id,
            @AuthenticationPrincipal String funcionarioId) {
        try {
            byte[] arquivoZip = certificadoService.downloadCertificado(id, funcionarioId);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(arquivoZip);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCertificado(
            @PathVariable String id,
            @AuthenticationPrincipal String funcionarioId) {
        
        Certificado certificado = certificadoService.buscarCertificadoPorId(id);
        
        if (!certificado.getFuncionario().getIdFuncionario().equals(funcionarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        certificadoService.deletarCertificado(id);
        return ResponseEntity.noContent().build();
    }
} 