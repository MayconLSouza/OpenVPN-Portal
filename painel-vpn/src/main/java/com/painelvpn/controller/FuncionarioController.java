package com.painelvpn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.painelvpn.model.Funcionario;
import com.painelvpn.service.FuncionarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funcionario> cadastrarFuncionario(@Valid @RequestBody Funcionario funcionario) {
        return ResponseEntity.ok(funcionarioService.cadastrarFuncionario(funcionario));
    }

    @PutMapping("/{id}/eleger-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funcionario> elegerAdministrador(@PathVariable String id) {
        return ResponseEntity.ok(funcionarioService.elegerAdministrador(id));
    }

    @PutMapping("/{id}/revogar-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funcionario> revogarAdministrador(@PathVariable String id) {
        return ResponseEntity.ok(funcionarioService.revogarAdministrador(id));
    }

    @PutMapping("/{id}/revogar-acesso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funcionario> revogarAcessoFuncionario(@PathVariable String id) {
        return ResponseEntity.ok(funcionarioService.revogarAcessoFuncionario(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerFuncionario(@PathVariable String id) {
        funcionarioService.removerFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reativar-acesso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funcionario> reativarAcessoFuncionario(@PathVariable String id) {
        return ResponseEntity.ok(funcionarioService.reativarAcessoFuncionario(id));
    }
} 