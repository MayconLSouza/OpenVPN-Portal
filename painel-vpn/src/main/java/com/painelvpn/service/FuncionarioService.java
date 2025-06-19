package com.painelvpn.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.painelvpn.model.Funcionario;
import com.painelvpn.repository.IFuncionarioRepository;
import com.painelvpn.enums.Enum_StatusFuncionario;

@Service
public class FuncionarioService {

    private final IFuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public FuncionarioService(
            IFuncionarioRepository funcionarioRepository,
            PasswordEncoder passwordEncoder,
            AuditoriaService auditoriaService) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Funcionario buscarPorId(String id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    @Transactional
    public Funcionario cadastrarFuncionario(Funcionario funcionario) {
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        Funcionario novoFuncionario = funcionarioRepository.save(funcionario);
        auditoriaService.registrarAcaoAdmin(
            "CADASTRO_FUNCIONARIO",
            "Cadastro do funcionário: " + novoFuncionario.getNome()
        );
        return novoFuncionario;
    }

    @Transactional
    public void removerFuncionario(String id) {
        try {
            Funcionario funcionario = buscarPorId(id);
            if (funcionario.getCertificados() != null && !funcionario.getCertificados().isEmpty()) {
                throw new RuntimeException("Não é possível remover o funcionário pois ele possui certificados associados");
            }
            funcionarioRepository.deleteById(id);
            auditoriaService.registrarAcaoAdmin(
                "REMOCAO_FUNCIONARIO",
                "Remoção do funcionário: " + funcionario.getNome()
            );
        } catch (Exception e) {
            System.err.println("Erro ao remover funcionário: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover funcionário: " + e.getMessage());
        }
    }

    @Transactional
    public Funcionario elegerAdministrador(String id) {
        try {
            Funcionario funcionario = buscarPorId(id);
            if (funcionario.getStatus() != Enum_StatusFuncionario.ATIVO) {
                throw new RuntimeException("Apenas funcionários ativos podem se tornar administradores");
            }
            funcionario.setAdmin(true);
            Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);
            auditoriaService.registrarAcaoAdmin(
                "ELEICAO_ADMIN",
                "Promoção do funcionário " + funcionario.getNome() + " a administrador"
            );
            return funcionarioAtualizado;
        } catch (Exception e) {
            System.err.println("Erro ao eleger administrador: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao eleger administrador: " + e.getMessage());
        }
    }

    @Transactional
    public Funcionario revogarAdministrador(String id) {
        try {
            Funcionario funcionario = buscarPorId(id);
            if (!funcionario.isAdmin()) {
                throw new RuntimeException("O funcionário não é um administrador");
            }
            funcionario.setAdmin(false);
            Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);
            auditoriaService.registrarAcaoAdmin(
                "REVOGACAO_ADMIN",
                "Revogação de privilégios de administrador do funcionário: " + funcionario.getNome()
            );
            return funcionarioAtualizado;
        } catch (Exception e) {
            System.err.println("Erro ao revogar administrador: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao revogar administrador: " + e.getMessage());
        }
    }

    @Transactional
    public Funcionario revogarAcessoFuncionario(String id) {
        try {
            Funcionario funcionario = buscarPorId(id);
            if (funcionario.getStatus() == Enum_StatusFuncionario.REVOGADO) {
                throw new RuntimeException("O acesso do funcionário já está revogado");
            }
            funcionario.setStatus(Enum_StatusFuncionario.REVOGADO);
            Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);
            auditoriaService.registrarAcaoAdmin(
                "REVOGACAO_ACESSO",
                "Revogação de acesso do funcionário: " + funcionario.getNome()
            );
            return funcionarioAtualizado;
        } catch (Exception e) {
            System.err.println("Erro ao revogar acesso do funcionário: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao revogar acesso do funcionário: " + e.getMessage());
        }
    }

    @Transactional
    public Funcionario reativarAcessoFuncionario(String id) {
        Funcionario funcionario = buscarPorId(id);
        if (funcionario.getStatus() == Enum_StatusFuncionario.ATIVO) {
            throw new RuntimeException("O acesso do funcionário já está ativo");
        }
        funcionario.setStatus(Enum_StatusFuncionario.ATIVO);
        funcionario.resetarTentativasLogin(); // Reseta as tentativas de login ao reativar
        Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);
        auditoriaService.registrarAcaoAdmin(
            "REATIVACAO_ACESSO",
            "Reativação de acesso do funcionário: " + funcionario.getNome()
        );
        return funcionarioAtualizado;
    }
} 