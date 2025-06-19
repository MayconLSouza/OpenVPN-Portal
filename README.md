# VPN Panel

Um painel de gerenciamento de certificados VPN para organizações. Os funcionários podem gerar ou revogar seus próprios certificados OpenVPN, eliminando o risco de credenciais compartilhadas. Os administradores supervisionam o acesso dos usuários enquanto delegam o gerenciamento de certificados, agilizando a segurança para o trabalho remoto.
Este sistema é composto por um back-end em Spring Boot e um front-end em Angular.
---

## Descrição Geral

- **Back-end (Spring Boot):** API RESTful responsável pela autenticação, controle de funcionários, gerenciamento de certificados VPN e registro de auditoria.
- **Front-end (Angular):** Interface web moderna e responsiva para administração dos certificados, funcionários e visualização de logs de auditoria.

---

## Estrutura de Pastas

### Back-end (`painel-vpn/`)

```
src/main/java/com/painelvpn/
├── config/         # Configurações de segurança e JWT
├── controller/     # Controladores REST (endpoints)
├── enums/          # Enumerações de domínio
├── model/          # Entidades JPA (modelos de dados)
├── repository/     # Interfaces de acesso a dados (JPA)
├── service/        # Lógica de negócio e serviços
└── PainelVpnApplication.java # Classe principal Spring Boot
```

### Front-end (`interface-angular-vpn-panel/vpn-panel/`)

```
src/app/
├── core/           # Serviços, guards, interceptors e models
├── features/       # Módulos de funcionalidades (funcionários, certificados, auditoria, dashboard)
├── layout/         # Componentes de layout (navbar, sidebar, main-layout)
├── auth/           # Módulo e componentes de autenticação
└── shared/         # Módulos/componentes compartilhados
```

---

## Divisão das Funcionalidades

### Back-end (Spring Boot)

- **config/**  
  - `SecurityConfig.java`: Configuração de segurança (Spring Security, CORS, etc).
  - `JwtAuthenticationFilter.java`: Filtro para autenticação JWT.

- **controller/**  
  - `AuthController.java`: Endpoints de autenticação (login).
  - `FuncionarioController.java`: Endpoints CRUD de funcionários.

- **service/**  
  - `AuthService.java`: Lógica de autenticação e geração de tokens.
  - `FuncionarioService.java`: Lógica de negócio para funcionários.
  - `CertificadoService.java`: Gerenciamento de certificados VPN.
  - `AuditoriaService.java`: Registro e consulta de logs de auditoria.
  - `JwtService.java`: Utilitários para manipulação de JWT.

- **model/**  
  - `Funcionario.java`, `Certificado.java`, `LogAuditoria.java`: Entidades do sistema.

- **repository/**  
  - `IFuncionarioRepository.java`, `ICertificadoRepository.java`, `ILogAuditoriaRepository.java`: Interfaces JPA para persistência.

- **enums/**  
  - `Enum_StatusFuncionario.java`: Enumeração de status de funcionário.

---

### Front-end (Angular)

- **core/**
  - `services/`: Serviços para autenticação, comunicação com API, etc.
  - `guards/`: Proteção de rotas (ex: `auth.guard.ts`).
  - `interceptors/`: Interceptação de requisições HTTP (ex: JWT).
  - `models/`: Modelos de dados TypeScript.

- **features/**
  - `funcionarios/`: Listagem e gerenciamento de funcionários.
  - `certificados/`: Listagem e gerenciamento de certificados VPN.
  - `auditoria/`: Visualização de logs de auditoria.
  - `dashboard/`: Visão geral do sistema.

- **layout/**
  - `navbar/`, `sidebar/`, `main-layout/`: Componentes de navegação e layout.

- **auth/**
  - `login/`: Tela e lógica de login.

- **shared/**
  - Componentes e módulos reutilizáveis.

---

## Funcionalidades Principais

- Autenticação JWT
- Gerenciamento de funcionários
- Gerenciamento de certificados VPN
- Log de auditoria
- Dashboard administrativo
- Segurança com roles, CORS, cookies HttpOnly
- Design responsivo e tema claro/escuro

---

## Como rodar o projeto
### Utilizando a VM (Virtual Machine) Tomcat

#### Para subir o Back - End:
```
  sudo systemctl daemon-reload
  sudo systemctl restart painel-vpn
```
#### Para subir o Front - End:
```
  ng build --configuration production
  sudo nginx -t
  sudo systemctl restart nginx
```

---

### Sem a utilização da VM (Virtual Machine)
#### Back-end (Spring Boot)

1. Configure o banco de dados MySQL e as variáveis de ambiente em `application.properties`.
2. Compile e execute o projeto com Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

#### Front-end (Angular)

1. Instale as dependências:
   ```bash
   cd interface-angular-vpn-panel/vpn-panel
   npm install
   ```
2. Configure o arquivo de ambiente (`src/environments/environment.ts`).
3. Rode o servidor de desenvolvimento:
   ```bash
   npm start
   ```

---

## Licença

Consulte o arquivo LICENSE para mais detalhes. 
