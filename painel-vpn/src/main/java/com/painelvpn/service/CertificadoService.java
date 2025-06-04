package com.painelvpn.service;

import java.util.List;

import com.painelvpn.model.Certificado;
import com.painelvpn.repository.ICertificadoRepository;

public class CertificadoService {

    private final ICertificadoRepository certificadoRepository;

    //Contrutores
    public CertificadoService(ICertificadoRepository certificadoRepository) {
        this.certificadoRepository = certificadoRepository;
    }

    //Métodos
    public Certificado criarCertificado(Certificado certificado) {
        return certificadoRepository.save(certificado);
    }

    public Certificado buscarCertificadoPorId(String id) {
        return certificadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));
    }

    
    public Certificado atualizarCertificado(String id, Certificado certificado) {
        return certificadoRepository.save(certificado);
    }
    
    public void deletarCertificado(String id) {
        certificadoRepository.deleteById(id);
    }
    
    public List<Certificado> buscarTodosCertificados() {
        return certificadoRepository.findAll();
    }
    
}
