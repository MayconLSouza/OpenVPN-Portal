package com.painelvpn.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.painelvpn.model.Certificado;
import com.painelvpn.model.Funcionario;
import com.painelvpn.repository.ICertificadoRepository;

@Service
public class CertificadoService {

    private static final Logger logger = LoggerFactory.getLogger(CertificadoService.class);
    private final ICertificadoRepository certificadoRepository;
    private final String pythonScriptPath;
    private final String certificadosPath;
    private final String deleteScriptPath;

    public CertificadoService(
        ICertificadoRepository certificadoRepository,
        @Value("${vpn.python.script.path}") String pythonScriptPath,
        @Value("${vpn.certificados.path}") String certificadosPath,
        @Value("${vpn.delete.script.path}") String deleteScriptPath
    ) {
        this.certificadoRepository = certificadoRepository;
        this.pythonScriptPath = pythonScriptPath;
        this.certificadosPath = certificadosPath;
        this.deleteScriptPath = deleteScriptPath;
        
        logger.info("CertificadoService inicializado com:");
        logger.info("Python Script Path: {}", pythonScriptPath);
        logger.info("Delete Script Path: {}", deleteScriptPath);
        logger.info("Certificados Path: {}", certificadosPath);
    }

    @Transactional
    public Certificado criarCertificado(Funcionario funcionario) throws IOException {
        String id = gerarIdentificadorUnico();
        logger.info("Gerando certificado com ID: {}", id);
        
        // Verifica se o script existe
        File scriptFile = new File(pythonScriptPath);
        if (!scriptFile.exists()) {
            logger.error("Script Python não encontrado em: {}", pythonScriptPath);
            throw new RuntimeException("Script Python não encontrado");
        }
        
        // Executa o script Python para gerar os arquivos
        ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScriptPath, id);
        processBuilder.redirectErrorStream(true);
        
        logger.info("Executando comando: python3 {} {}", pythonScriptPath, id);
        
        Process process = processBuilder.start();
        
        // Captura a saída do processo
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        try {
            int exitCode = process.waitFor();
            logger.info("Script Python finalizado com código: {}", exitCode);
            logger.info("Saída do script:\n{}", output.toString());
            
            if (exitCode != 0) {
                logger.error("Erro ao gerar arquivos VPN. Código de saída: {}", exitCode);
                throw new RuntimeException("Erro ao gerar arquivos VPN: " + output.toString());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Processo interrompido ao gerar arquivos VPN", e);
            throw new RuntimeException("Processo interrompido ao gerar arquivos VPN", e);
        }

        // Verifica se o arquivo ZIP foi gerado
        File zipFile = new File(certificadosPath + "/" + id + ".zip");
        if (!zipFile.exists()) {
            logger.error("Arquivo ZIP não foi gerado em: {}", zipFile.getAbsolutePath());
            throw new RuntimeException("Arquivo ZIP não foi gerado");
        }

        // Cria o certificado no banco de dados
        Certificado certificado = new Certificado();
        certificado.setId(id);
        certificado.setDataCriacao(LocalDate.now());
        certificado.setDataValidade(LocalDate.now().plusDays(7));
        certificado.setCaminhoLinux(certificadosPath + "/" + id + ".zip");
        certificado.setFuncionario(funcionario);

        logger.info("Salvando certificado no banco de dados: {}", certificado);
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
    
    @Transactional
    public void deletarCertificado(String id) {
        logger.info("Iniciando processo de remoção do certificado: {}", id);
        
        // Verifica se o script existe
        File scriptFile = new File(deleteScriptPath);
        if (!scriptFile.exists()) {
            logger.error("Script Python de remoção não encontrado em: {}", deleteScriptPath);
            throw new RuntimeException("Script Python de remoção não encontrado");
        }
        
        try {
            // Executa o script Python para remover os arquivos
            ProcessBuilder processBuilder = new ProcessBuilder("python3", deleteScriptPath, id);
            processBuilder.redirectErrorStream(true);
            
            logger.info("Executando comando: python3 {} {}", deleteScriptPath, id);
            
            Process process = processBuilder.start();
            
            // Captura a saída do processo
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            int exitCode = process.waitFor();
            logger.info("Script Python de remoção finalizado com código: {}", exitCode);
            logger.info("Saída do script:\n{}", output.toString());
            
            if (exitCode != 0) {
                logger.error("Erro ao remover arquivos do certificado. Código de saída: {}", exitCode);
                throw new RuntimeException("Erro ao remover arquivos do certificado: " + output.toString());
            }
            
            // Remove o registro do banco de dados
            certificadoRepository.deleteById(id);
            logger.info("Certificado removido com sucesso: {}", id);
            
        } catch (IOException | InterruptedException e) {
            logger.error("Erro ao executar script de remoção", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Erro ao remover certificado", e);
        }
    }
    
    public List<Certificado> buscarTodosCertificados() {
        return certificadoRepository.findAll();
    }
}
