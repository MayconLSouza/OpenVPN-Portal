# VPN Panel

Interface web para gerenciamento de certificados VPN e controle de acesso de funcionários.

## 🚀 Tecnologias

- Angular 17
- Angular Material
- JWT Authentication
- RxJS
- SCSS

## 📋 Requisitos

- Node.js 18+
- npm 9+
- Angular CLI 17+

## 🔧 Instalação

1. Clone o repositório
```bash
git clone [URL_DO_REPOSITORIO]
cd vpn-panel
```

2. Instale as dependências
```bash
npm install
```

3. Configure as variáveis de ambiente
- Copie o arquivo `src/environments/environment.example.ts` para `src/environments/environment.ts`
- Ajuste as configurações conforme necessário

4. Inicie o servidor de desenvolvimento
```bash
npm start
```

## 🏗 Estrutura do Projeto

```
src/
├── app/
│   ├── core/               # Serviços, guardas e interceptadores
│   ├── features/           # Módulos de funcionalidades
│   ├── layout/            # Componentes de layout
│   └── shared/            # Módulos e componentes compartilhados
├── assets/                # Recursos estáticos
└── environments/          # Configurações de ambiente
```

## 🔐 Autenticação

O sistema utiliza JWT (JSON Web Token) para autenticação. O token é armazenado em um cookie HttpOnly para maior segurança.

## 👥 Funcionalidades

- Login com autenticação JWT
- Dashboard com visão geral
- Gerenciamento de certificados VPN
- Controle de funcionários
- Log de auditoria
- Tema claro/escuro
- Design responsivo

## 🔄 Integração com Backend

A aplicação se comunica com uma API REST Spring Boot através dos seguintes endpoints:

- `POST /api/auth/login` - Autenticação
- `GET/POST/PUT/DELETE /api/certificados` - CRUD de certificados
- `GET/POST/PUT/DELETE /api/funcionarios` - CRUD de funcionários
- `GET /api/auditoria` - Log de auditoria

## 🛡 Segurança

- Autenticação JWT
- Cookies HttpOnly
- CORS configurado
- Rotas protegidas
- Controle de acesso baseado em roles
- Validação de inputs
- Sanitização de dados

## 📦 Build

Para gerar uma build de produção:

```bash
npm run build
```

Os arquivos serão gerados no diretório `dist/`.

## 🧪 Testes

Para executar os testes unitários:

```bash
npm test
```

## 👥 Autores

- [Seu Nome]

## 📄 Licença

Este projeto está sob a licença [TIPO_DE_LICENCA] - veja o arquivo LICENSE.md para detalhes. 