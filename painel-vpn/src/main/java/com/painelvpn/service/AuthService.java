package com.painelvpn.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.painelvpn.model.Funcionario;
import com.painelvpn.repository.IFuncionarioRepository;
import com.painelvpn.enums.Enum_StatusFuncionario;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final IFuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IFuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Tentando carregar usuário: {}", username);
        
        Funcionario funcionario = funcionarioRepository.findByUsuario(username)
            .orElseThrow(() -> {
                logger.error("Usuário não encontrado: {}", username);
                return new UsernameNotFoundException("Usuário ou senha estão incorretos");
            });

        if (funcionario.getStatus() != Enum_StatusFuncionario.ATIVO) {
            logger.error("Usuário bloqueado ou revogado - Username: {}, Status: {}", username, funcionario.getStatus());
            throw new DisabledException("Entre em contato com o administrador da rede");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (funcionario.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        logger.debug("Usuário {} carregado com sucesso. Admin: {}", username, funcionario.isAdmin());
        return new User(funcionario.getUsuario(), funcionario.getSenha(), authorities);
    }

    public void handleFailedLogin(String username) {
        logger.debug("Registrando falha de login para usuário: {}", username);
        funcionarioRepository.findByUsuario(username).ifPresent(funcionario -> {
            funcionario.incrementarTentativasLogin();
            if (funcionario.getTentativasLogin() >= 10) {
                funcionario.setStatus(Enum_StatusFuncionario.BLOQUEADO);
                logger.warn("Usuário {} bloqueado após {} tentativas de login", username, funcionario.getTentativasLogin());
            }
            funcionarioRepository.save(funcionario);
            logger.debug("Tentativas de login atualizadas para {}: {}", username, funcionario.getTentativasLogin());
        });
    }

    public void handleSuccessfulLogin(String username) {
        logger.debug("Registrando login bem-sucedido para usuário: {}", username);
        funcionarioRepository.findByUsuario(username).ifPresent(funcionario -> {
            funcionario.resetarTentativasLogin();
            funcionarioRepository.save(funcionario);
            logger.debug("Tentativas de login resetadas para {}", username);
        });
    }

    public boolean verificarSenha(String rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.debug("Verificação de senha para usuário - Resultado: {}", matches);
        return matches;
    }
} 