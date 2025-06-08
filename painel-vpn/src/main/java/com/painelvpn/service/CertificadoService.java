package com.painelvpn.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.painelvpn.model.Certificado;
import com.painelvpn.model.Funcionario;
import com.painelvpn.repository.ICertificadoRepository;

@Service
public class CertificadoService {

    private final ICertificadoRepository certificadoRepository;
    private final String pythonScriptPath;
    private final String certificadosPath;

    public CertificadoService(
        ICertificadoRepository certificadoRepository,
        @Value("${vpn.python.script.path}") String pythonScriptPath,
        @Value("${vpn.certificados.path}") String certificadosPath
    ) {
        this.certificadoRepository = certificadoRepository;
        this.pythonScriptPath = pythonScriptPath;
        this.certificadosPath = certificadosPath;
    }

    @Transactional
    public Certificado criarCertificado(Funcionario funcionario) throws IOException {
        String id = gerarIdentificadorUnico();
        
        // Executa o script Python para gerar os arquivos
        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, id);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Erro ao gerar arquivos VPN");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processo interrompido ao gerar arquivos VPN", e);
        }

        // Cria o certificado no banco de dados
        Certificado certificado = new Certificado();
        certificado.setId(id);
        certificado.setDataCriacao(LocalDate.now());
        certificado.setDataValidade(LocalDate.now().plusDays(7));
        certificado.setCaminhoLinux(certificadosPath + "/" + id + ".zip");
        certificado.setFuncionario(funcionario);

        return certificadoRepository.save(certificado);
    }

    public byte[] downloadCertificado(String id, String funcionarioId) throws IOException {
        Certificado certificado = certificadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));

        // Verifica se o certificado pertence ao funcionário
        if (!certificado.getFuncionario().getIdFuncionario().equals(funcionarioId)) {
            throw new RuntimeException("Acesso negado ao certificado");
        }

        // Lê o arquivo .zip
        Path zipPath = Path.of(certificado.getCaminhoLinux());
        if (!Files.exists(zipPath)) {
            throw new RuntimeException("Arquivo do certificado não encontrado");
        }

        return Files.readAllBytes(zipPath);
    }

    public List<Certificado> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return certificadoRepository.findByDataCriacaoBetween(inicio, fim);
    }

    public List<Certificado> buscarPorIdentificador(String identificador) {
        return certificadoRepository.findByIdContainingIgnoreCase(identificador);
    }

    public List<Certificado> buscarPorFuncionario(String funcionarioId) {
        return certificadoRepository.findByFuncionarioId(funcionarioId);
    }

    private String gerarIdentificadorUnico() {
        String id;
        do {
            // Gera um identificador alfanumérico de 7 caracteres
            id = UUID.randomUUID().toString()
                    .replaceAll("-", "")
                    .substring(0, 7)
                    .toUpperCase();
        } while (certificadoRepository.existsById(id));
        return id;
    }

    // Métodos existentes mantidos...
    public Certificado buscarCertificadoPorId(String id) {
        return certificadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));
    }
    
    public void deletarCertificado(String id) {
        certificadoRepository.deleteById(id);
    }
    
    public List<Certificado> buscarTodosCertificados() {
        return certificadoRepository.findAll();
    }
}
