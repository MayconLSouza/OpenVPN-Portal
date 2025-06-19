package com.painelvpn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.painelvpn.model.Funcionario;
import com.painelvpn.service.FuncionarioService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Funcionario>> listarFuncionarios() {
        return ResponseEntity.ok(funcionarioService.listarTodos());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funcionario> cadastrarFuncionario(@Valid @RequestBody Funcionario funcionario) {
        return ResponseEntity.ok(funcionarioService.cadastrarFuncionario(funcionario));
    }

    @PutMapping("/{id}/eleger-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> elegerAdministrador(@PathVariable("id") String idFuncionario) {
        try {
            Funcionario funcionario = funcionarioService.elegerAdministrador(idFuncionario);
            return ResponseEntity.ok(funcionario);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/revogar-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> revogarAdministrador(@PathVariable("id") String idFuncionario) {
        try {
            Funcionario funcionario = funcionarioService.revogarAdministrador(idFuncionario);
            return ResponseEntity.ok(funcionario);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/revogar-acesso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> revogarAcessoFuncionario(@PathVariable("id") String idFuncionario) {
        try {
            Funcionario funcionario = funcionarioService.revogarAcessoFuncionario(idFuncionario);
            return ResponseEntity.ok(funcionario);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removerFuncionario(@PathVariable("id") String idFuncionario) {
        try {
            funcionarioService.removerFuncionario(idFuncionario);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reativar-acesso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funcionario> reativarAcessoFuncionario(@PathVariable String id) {
        return ResponseEntity.ok(funcionarioService.reativarAcessoFuncionario(id));
    }
} 