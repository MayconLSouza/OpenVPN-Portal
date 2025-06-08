package com.painelvpn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailRedefinicaoSenha(String emailDestino, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(emailDestino);
            helper.setSubject("Redefinição de Senha - Painel VPN");

            String conteudo = String.format("""
                <html>
                <body>
                    <h2>Redefinição de Senha</h2>
                    <p>Foi solicitada a redefinição de senha para sua conta no Painel VPN.</p>
                    <p>Use o token abaixo para redefinir sua senha:</p>
                    <p style="background-color: #f0f0f0; padding: 10px; font-family: monospace; font-size: 16px;">%s</p>
                    <p>Este token é válido por 1 hora e pode ser usado apenas uma vez.</p>
                    <p>Se você não solicitou esta redefinição, por favor ignore este e-mail.</p>
                    <p>Atenciosamente,<br>Equipe Painel VPN</p>
                </body>
                </html>
                """, token);

            helper.setText(conteudo, true);
            mailSender.send(message);
            logger.info("E-mail de redefinição de senha enviado para: {}", emailDestino);
        } catch (MessagingException e) {
            logger.error("Erro ao enviar e-mail de redefinição de senha para: {}", emailDestino, e);
            throw new RuntimeException("Erro ao enviar e-mail de redefinição de senha", e);
        }
    }
} 