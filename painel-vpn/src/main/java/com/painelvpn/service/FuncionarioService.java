package com.painelvpn.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.painelvpn.model.Funcionario;
import com.painelvpn.repository.IFuncionarioRepository;

@Service
public class FuncionarioService {

    private final IFuncionarioRepository funcionarioRepository;

    public FuncionarioService(IFuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Funcionario buscarPorId(String id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    public Funcionario salvar(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void remover(String id) {
        Funcionario funcionario = buscarPorId(id);
        // Verifica se o funcionário tem certificados antes de remover
        if (funcionario.getCertificados() != null && !funcionario.getCertificados().isEmpty()) {
            throw new RuntimeException("Não é possível remover o funcionário pois ele possui certificados associados");
        }
        funcionarioRepository.deleteById(id);
    }
} 