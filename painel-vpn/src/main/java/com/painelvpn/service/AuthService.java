package com.painelvpn.service;

import org.springframework.security.authentication.AuthenticationManager;
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
                return new UsernameNotFoundException("Usuário não encontrado: " + username);
            });

        if (funcionario.getStatus() != Enum_StatusFuncionario.ATIVO) {
            logger.error("Usuário bloqueado ou revogado: {}", username);
            throw new UsernameNotFoundException("Usuário bloqueado ou revogado: " + username);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (funcionario.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        logger.debug("Usuário {} carregado com sucesso. Admin: {}", username, funcionario.isAdmin());
        return new User(funcionario.getUsuario(), funcionario.getSenha(), authorities);
    }

    public boolean verificarSenha(String rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.debug("Verificação de senha para usuário - Resultado: {}", matches);
        return matches;
    }
} 