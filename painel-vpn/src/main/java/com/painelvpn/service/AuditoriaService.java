package com.painelvpn.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.painelvpn.model.LogAuditoria;
import com.painelvpn.repository.ILogAuditoriaRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuditoriaService {

    private final ILogAuditoriaRepository logAuditoriaRepository;

    public AuditoriaService(ILogAuditoriaRepository logAuditoriaRepository) {
        this.logAuditoriaRepository = logAuditoriaRepository;
    }

    public void registrarAcaoAdmin(String acao, String detalhes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();

            String ip = request.getRemoteAddr();
            String idFuncionario = auth.getName(); // Assumindo que o nome é o ID do funcionário
            String nomeFuncionario = auth.getName(); // Você pode melhorar isso para obter o nome real

            LogAuditoria log = new LogAuditoria(acao, detalhes, idFuncionario, nomeFuncionario, ip);
            logAuditoriaRepository.save(log);
        }
    }
} 