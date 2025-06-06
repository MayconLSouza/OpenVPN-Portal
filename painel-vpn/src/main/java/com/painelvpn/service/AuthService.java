package com.painelvpn.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.painelvpn.model.Funcionario;
import com.painelvpn.repository.IFuncionarioRepository;
import com.painelvpn.enums.Enum_StatusFuncionario;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService implements UserDetailsService {

    private final IFuncionarioRepository funcionarioRepository;

    public AuthService(IFuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Funcionario funcionario = funcionarioRepository.findByUsuario(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        if (funcionario.getStatus() != Enum_StatusFuncionario.ATIVO) {
            throw new UsernameNotFoundException("Usuário bloqueado ou revogado: " + username);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (funcionario.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(funcionario.getUsuario(), funcionario.getSenha(), authorities);
    }
} 