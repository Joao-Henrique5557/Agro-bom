# 🌱 AgroBom

Sistema de gestão de estoque, pedidos e solicitações de compra para a **AGROBOM**,
revendedora de produtos agrícolas.

O projeto tem três partes, neste monorepo:

```
AgroBom/
├── backend/     Java + Maven (Jakarta Servlets/JSP) + API REST + testes JUnit
├── frontend/    App mobile em React Native (Expo)
├── docker/      Script de inicialização do MySQL usado pelo docker-compose
└── docker-compose.yml
```

- **Backend web (JSP)**: as telas originais (`/clientes`, `/produtos`, `/pedidos`,
  `/fornecedores`, `/solicitacoes`, `/relatorios`) continuam funcionando normalmente,
  renderizadas em HTML pelo próprio servidor.
- **API REST (JSON)**: novos endpoints em `/api/*` (`ClienteApiServlet`,
  `ProdutoApiServlet` etc.) que reaproveitam os mesmos DAOs e são consumidos pelo
  app React Native.
- **App mobile**: React Native (Expo) com 5 telas principais — Dashboard, Estoque,
  Pedidos, Fornecedores e Relatórios (Clientes e Solicitações ficam a um toque de
  distância, dentro dessas abas).

---

## 1. Subindo tudo com Docker (recomendado)

Pré-requisitos: [Docker](https://www.docker.com/) e Docker Compose.

```bash
cd AgroBom
cp .env.example .env        # opcional, ajuste usuário/senha se quiser
docker compose up --build
```

Isso sobe 3 containers:

| Serviço      | URL/Porta             | Descrição                                                                              |
| ------------ | --------------------- | -------------------------------------------------------------------------------------- |
| `mysql`      | `localhost:3306`      | Banco `db_agro_bom`, schema criado automaticamente a partir de `docker/mysql/init.sql` |
| `phpmyadmin` | http://localhost:8081 | Interface web para inspecionar o banco                                                 |
| `backend`    | http://localhost:8080 | Tomcat 11 rodando o WAR gerado pelo Maven                                              |

Depois de subir, acesse:

- Telas web: **http://localhost:8080/**
- API REST: **http://localhost:8080/api/dashboard**, `/api/produtos`, `/api/clientes` etc.

Para derrubar tudo: `docker compose down` (adicione `-v` para apagar também os dados do MySQL).

> O `docker-compose.yml` builda a imagem do backend a partir de `backend/Dockerfile`,
> que faz `mvn clean package` dentro de um container Maven e depois copia o `.war`
> gerado para uma imagem Tomcat 11 limpa — não é necessário ter Java/Maven instalados
> na sua máquina para isso, apenas o Docker.

---

## 2. Rodando o backend manualmente (Eclipse / Maven local)

### 2.1 Banco de dados

Se não for usar o `docker compose`, suba um MySQL local e rode o script:

```bash
mysql -u root -p < docker/mysql/init.sql
```

### 2.2 Build com Maven

```bash
cd backend
mvn clean package
```

Isso gera `target/agrobom-backend.war`. Copie esse `.war` para a pasta
`webapps` do seu Tomcat 10+/11 (renomeando para `ROOT.war` se quiser servir
na raiz), ou importe o projeto no Eclipse como **Maven Project** (o `pom.xml`
já descreve toda a estrutura — não são mais necessários os arquivos
`.classpath`/`.project`/`.settings` do Eclipse antigo, embora o projeto
continue abrindo normalmente lá).

### 2.3 Configuração da conexão

O `ConnectionFactory` lê a conexão de variáveis de ambiente, com valores
padrão para desenvolvimento local:

| Variável      | Padrão        |
| ------------- | ------------- |
| `DB_HOST`     | `localhost`   |
| `DB_PORT`     | `3306`        |
| `DB_NAME`     | `db_agro_bom` |
| `DB_USER`     | `root`        |
| `DB_PASSWORD` | `1234`        |

Rodando fora do Docker, ou exporte essas variáveis antes de subir o Tomcat,
ou simplesmente garanta que seu MySQL local usa usuário `root`/senha `1234`
no banco `db_agro_bom` (mesmos valores do `banco.sql` original).

### 2.4 Rodando os testes unitários

```bash
cd backend
mvn test
```

Os testes usam **JUnit 5** + **Mockito** (incluindo `mockStatic` para simular
o `ConnectionFactory`), então **não precisam de um banco de dados real** —
`Connection`, `PreparedStatement` e `ResultSet` são todos mockados. Cobertura
atual:

- `ClienteDAOTest`, `ProdutoDAOTest`: `salvar`, `listarTodos`, `buscarPorId`/`buscarPorCpf`, `remover`
- `ClienteTest`, `ProdutoTest`: getters/setters e a regra de estoque crítico
- `RelatorioServletTest`: parsing de parâmetros de mês/ano com valores ausentes/ inválidos

> ⚠️ Este ambiente de geração de código não tinha acesso ao Maven Central para
> baixar as dependências e rodar `mvn test` de fato — revise o `pom.xml` e rode
> localmente para confirmar. As versões usadas (`junit-jupiter 5.10.2`,
> `mockito-core 5.11.0`, `mysql-connector-j 8.4.0`, `gson 2.11.0`) são estáveis
> e amplamente usadas, mas vale conferir se há versões mais novas disponíveis.

---

## 3. API REST (usada pelo app React Native)

Todos os endpoints ficam sob `/api` e devolvem/recebem JSON. Reaproveitam os
mesmos DAOs das telas JSP.

| Recurso                | Endpoint                                                                                                     |
| ---------------------- | ------------------------------------------------------------------------------------------------------------ |
| Dashboard (contadores) | `GET /api/dashboard`                                                                                         |
| Clientes               | `GET/POST /api/clientes`, `GET/DELETE /api/clientes?cpf=...`                                                 |
| Produtos (Estoque)     | `GET/POST /api/produtos`, `GET/DELETE /api/produtos?id=...`                                                  |
| Fornecedores           | `GET/POST /api/fornecedores`, `GET/DELETE /api/fornecedores?id=...`                                          |
| Pedidos                | `GET/POST /api/pedidos`, `GET/DELETE /api/pedidos?id=...`, filtro `?mes=&ano=`                               |
| Solicitações de compra | `GET/POST /api/solicitacoes`, `GET/DELETE /api/solicitacoes?id=...`                                          |
| Relatórios (1 a 6)     | `GET /api/relatorios?rel=1` … `?rel=6` (params extras: `mes`/`ano`, `mes5`/`ano5`, `data_inicio`/`data_fim`) |

`POST` cria um novo registro quando o corpo não traz `id` (ou traz `id <= 0`)
e atualiza quando o `id` é informado — mesmo comportamento de "upsert" das
telas JSP.

Exemplo rápido:

```bash
curl http://localhost:8080/api/dashboard
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Adubo NPK","descricao":"20-05-20","preco":89.9,"unidade_medida":"kg","quantidade_estoque":100,"quantidade_ideal":30}'
```

---

## 4. App mobile (React Native / Expo)

### 4.1 Instalação

```bash
cd frontend
npm install
```

### 4.2 Apontando para o backend

Edite `frontend/app.json` → `expo.extra.apiBaseUrl`:

| Onde você está rodando o app | `apiBaseUrl`                                  |
| ---------------------------- | --------------------------------------------- |
| Emulador Android             | `http://10.0.2.2:8080/api`                    |
| Simulador iOS                | `http://localhost:8080/api`                   |
| Dispositivo físico (Expo Go) | `http://<IP-da-sua-máquina-na-rede>:8080/api` |

(`10.0.2.2` é o alias que o emulador Android usa para acessar o `localhost`
da máquina host — por isso é o valor padrão já configurado.)

### 4.3 Rodando

```bash
npm start          # abre o Expo Dev Tools / QR code (Expo Go)
npm run android     # emulador/dispositivo Android
npm run ios         # simulador iOS (necessário macOS)
npm run web          # versão web, útil para testar rápido
```

### 4.4 Telas

- **Dashboard** — contadores gerais (clientes, fornecedores, produtos, pedidos,
  solicitações) vindos de `/api/dashboard`, com atalhos para as demais telas
  e para Clientes.
- **Estoque** — lista de produtos com indicador de estoque crítico (mesma regra
  visual do site: `estoque <= ideal`), formulário de cadastro e remoção.
- **Pedidos** — lista + formulário de novo pedido, com seleção do cliente por
  nome (dropdown alimentado por `/api/clientes`).
- **Fornecedores** — lista + cadastro de fornecedores, com acesso, dentro da
  mesma aba, à tela de **Solicitações de Compra** (uma solicitação sempre
  pertence a um fornecedor).
- **Relatórios** — os 6 relatórios gerenciais do enunciado, cada um com seus
  próprios filtros (mês/ano ou intervalo de datas) e resultado renderizado
  em cards.

> O app React Native fala apenas com a API REST (`/api/*`) — as telas JSP
> continuam existindo separadamente para acesso via navegador.

---

## 5. Estrutura do banco de dados

Ver `docker/mysql/init.sql` (mesmo conteúdo usado pelo `docker-compose`, também
disponível originalmente em `database/banco.sql`).

| Tabela                                    | Descrição                                          |
| ----------------------------------------- | -------------------------------------------------- |
| `CLIENTE`                                 | cadastro de clientes (chave: CPF)                  |
| `PRODUTO`                                 | catálogo de produtos, com estoque atual e ideal    |
| `FORNECEDOR`                              | cadastro de fornecedores                           |
| `PRODUTO_FORNECEDOR`                      | associação N:M produto ↔ fornecedor                |
| `PEDIDO` / `ITEM_PEDIDO`                  | pedidos de clientes e seus itens                   |
| `SOLICITACAO_COMPRA` / `ITEM_SOLICITACAO` | solicitações de compra a fornecedores e seus itens |

## 6. Relatórios gerenciais implementados

1. **Posição de estoque** — descrição, quantidade existente, unidade de medida e quantidade mínima ideal de cada produto.
2. **Pedidos por mês** — número do pedido, dados do cliente e produtos pedidos (descrição, unidade, quantidade), filtrado por mês/ano.
3. **Pedidos por intervalo de datas** — número, desconto e valor total de cada pedido no período.
4. **Fornecedores por produto** — nome, CNPJ e telefone de cada fornecedor de cada produto.
5. **Solicitações de compra por mês** — fornecedor, produtos/quantidades, número e situação (aberta/encerrada) de cada solicitação.
6. **Volume financeiro (12 meses)** — soma mensal de pedidos e solicitações, em R$, mês a mês.

---

## 7. Principais decisões técnicas

- **Conexão por método, nunca por instância**: todo DAO abre e fecha sua
  própria `Connection` via `try-with-resources` a cada chamada
  (`ConnectionFactory.getConnection()`), em vez de guardar a conexão como
  campo de instância — evita conexões "mortas"/expiradas em containers e deixa
  os DAOs simples de testar com Mockito.
- **Servlets sem estado**: cada `doGet`/`doPost` instancia os DAOs que precisa,
  em vez de guardá-los como campo preenchido em `init()`.
- **API REST enxuta**: os endpoints `/api/*` são servlets separados das telas
  JSP, reaproveitando os mesmos DAOs — não há duplicação de lógica de acesso a
  dados entre o site e o app mobile.
- **Configuração via variáveis de ambiente**: o mesmo artefato (`.war`) roda
  tanto localmente (valores padrão) quanto em Docker (variáveis definidas no
  `docker-compose.yml`), sem precisar recompilar.

## 8. Próximos passos sugeridos

- Autenticação/autorização (hoje a API é aberta) antes de qualquer deploy público.
- Endpoints REST para os itens de pedido/solicitação (`ITEM_PEDIDO`,
  `ITEM_SOLICITACAO`), hoje só manipulados pelas telas JSP através dos DAOs
  correspondentes — o app mobile registra pedidos/solicitações apenas com o
  valor total informado manualmente, no mesmo formato do formulário web atual.
- Testes de integração com Testcontainers (MySQL real em container) complementando
  os testes unitários com mocks.
- Publicação do app com EAS Build para gerar `.apk`/`.ipa` de instalação direta.

---

**Autor:** João Henrique da Silva
**Projeto:** exercício acadêmico de backend + mobile para gestão agrícola.
