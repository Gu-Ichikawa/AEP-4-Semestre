# 🩸 HemoPlay

**Plataforma gamificada para incentivo à doação de sangue**

> AEP — 4º semestre de Análise e Desenvolvimento de Sistemas | UniCesumar | 2026
> **Integrantes:** Gustavo Cunha Ichikawa (RA 26007051-2) e João Marcos Poloni Demito (RA 26006680-2)

<p align="center">
  <img src="src/main/resources/static/img/mascote/cor-vermelha.jpg" alt="Mascote do HemoPlay, um morcego vermelho" width="180">
</p>

## Sumário

1. [Sobre o projeto](#-sobre-o-projeto)
2. [O que foi entregue na 2ª etapa](#-o-que-foi-entregue-na-2ª-etapa)
3. [Tecnologias](#️-tecnologias)
4. [Arquitetura e organização das pastas](#️-arquitetura-e-organização-das-pastas)
5. [Programação Orientada a Objetos](#-programação-orientada-a-objetos)
6. [Banco de dados](#️-banco-de-dados)
7. [Como preparar o ambiente e rodar o projeto](#-como-preparar-o-ambiente-e-rodar-o-projeto)
8. [Interface gráfica](#️-interface-gráfica)
9. [Testando a API com o Postman](#-testando-a-api-com-o-postman)
10. [Regras de gamificação](#-regras-de-gamificação)
11. [Ajustes em relação à documentação da 1ª entrega](#-ajustes-em-relação-à-documentação-da-1ª-entrega)
12. [Limitações conhecidas](#️-limitações-conhecidas)
13. [Requisitos funcionais](#-requisitos-funcionais)
14. [Cronograma](#-cronograma)
15. [Documentação de apoio](#-documentação-de-apoio)

---

## 📌 Sobre o projeto

Muitas pessoas têm vontade de doar sangue, mas não sabem quais são os requisitos, onde doar ou quais campanhas estão ativas. E quem doa uma vez raramente recebe algum incentivo para voltar.

O HemoPlay junta essas informações em um só lugar e usa elementos de jogos para manter o usuário envolvido com a causa: pontos, níveis, missões, conquistas, eventos sazonais e um mascote (um morcego) que pode ser personalizado com visuais desbloqueados.

A gamificação incentiva o engajamento **sem estimular doações fora do intervalo recomendado**. A plataforma informa quando o usuário poderá doar de novo e não premia doações antecipadas.

Os dados de hemocentros, campanhas e estoques de sangue são **simulados** e têm finalidade acadêmica. Eles não representam dados reais.

### 🎯 ODS

**ODS 3 — Saúde e Bem-Estar.** O projeto usa a tecnologia para informar, conscientizar e incentivar a doação voluntária de sangue.

---

## ✅ O que foi entregue na 2ª etapa

| Critério da avaliação | Onde está no projeto |
|---|---|
| **Aderência à proposta** | Os requisitos RF01 a RF14 da 1ª entrega foram implementados (veja a [tabela de requisitos](#-requisitos-funcionais)), com missões, conquistas, níveis, mascote, eventos e notificações. |
| **Orientação a Objetos** | Classe abstrata `Usuario` com três subclasses, a interface `RequisitoDesbloqueio` com cinco implementações, polimorfismo com `@Override` e encapsulamento das regras nas entidades (veja [Programação Orientada a Objetos](#-programação-orientada-a-objetos)). |
| **CRUD** | Usuário com Create, Read, Update e Delete completos. Doações com registro, confirmação, histórico e lista de pendentes. Mascote com consulta e alteração. |
| **Persistência em banco** | MySQL com 16 tabelas, chaves estrangeiras, restrições `CHECK` e `UNIQUE`, e acesso com Spring Data JPA. O Hibernate valida as entidades contra o banco na hora em que o sistema sobe. |

O foco da entrega é o **back-end e o banco de dados**. A interface gráfica foi feita como apoio para visualizar e testar o sistema (veja [Interface gráfica](#️-interface-gráfica)).

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Para que serve no projeto |
|---|---|---|
| Java | 21 | Linguagem do back-end |
| Spring Boot | 4.1.1 | Estrutura da aplicação, API REST e injeção de dependências |
| Spring Data JPA (Hibernate) | incluído no Spring Boot | Comunicação com o banco de dados |
| Jakarta Validation | incluído no Spring Boot | Validação dos dados recebidos (`@NotBlank`, `@Email`, `@Size`) |
| Spring Security Crypto | incluído no Spring Boot | Criptografia das senhas com BCrypt |
| MySQL | 8.0 | Banco de dados relacional |
| HTML, CSS e JavaScript | — | Interface gráfica de apoio, sem framework |
| Maven | wrapper incluído no projeto | Gerenciamento das dependências |
| Postman | — | Testes da API |
| Git e GitHub | — | Controle de versão |
| VS Code / IntelliJ IDEA | — | Desenvolvimento |

---

## 🏗️ Arquitetura e organização das pastas

O projeto segue a arquitetura **MVC complementada pelas camadas Service e Repository**, como foi definido na 1ª entrega:

```
Requisição (Postman ou interface)
        │
        ▼
  Controller  → recebe a requisição, valida o JSON e devolve a resposta
        │
        ▼
   Service    → aplica as regras de negócio (pontos, permissões, missões...)
        │
        ▼
  Repository  → lê e grava no banco usando Spring Data JPA
        │
        ▼
    MySQL
```

```
AEP-4-Semestre/
├── database/
│   ├── schema.sql          → cria o banco e as 16 tabelas
│   ├── seed.sql            → dados fixos: tipos sanguíneos e níveis
│   └── dados-teste.sql     → dados de exemplo: hemocentros, campanhas, missões, usuários...
├── docs/                   → documentação da 1ª entrega (PDF, DER e diagrama de classes)
├── src/main/java/com/hemoplay/
│   ├── controller/         → endpoints da API e tratamento de erros
│   ├── dto/                → objetos de entrada e saída da API (records)
│   ├── model/              → entidades JPA
│   │   └── requisito/      → interface RequisitoDesbloqueio e suas implementações
│   ├── repository/         → interfaces Spring Data JPA
│   ├── service/            → regras de negócio
│   └── HemoplayApplication.java → classe que inicia o sistema
├── src/main/resources/
│   ├── application.properties.example → modelo da configuração do banco
│   └── static/             → interface gráfica (HTML, CSS, JS e imagens do mascote)
└── pom.xml                 → dependências do projeto
```

---

## 🧩 Programação Orientada a Objetos

### Classe abstrata e herança

`Usuario` é uma **classe abstrata**. Não existe um "usuário genérico" no sistema: todo usuário é um `Doador`, um `Administrador` ou um `ResponsavelHemocentro`. As três subclasses herdam os atributos comuns (nome, e-mail, senha, pontuação, nível, tipo sanguíneo e mascote).

No banco, as três ficam na mesma tabela `usuario` (estratégia `SINGLE_TABLE`). A coluna `tipo_usuario` indica a subclasse de cada linha.

```java
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario")
public abstract class Usuario {
    public abstract List<String> obterPermissoes();

    public boolean possuiPermissao(String permissao) {
        return obterPermissoes().contains(permissao);
    }
}
```

### Polimorfismo 1: permissões de cada tipo de usuário

Cada subclasse sobrescreve `obterPermissoes()` com `@Override`:

| Classe | Permissões |
|---|---|
| `Doador` | registrarDoacao, consultarHistorico, personalizarMascote |
| `Administrador` | confirmarDoacao, gerenciarUsuarios, gerenciarCampanhas, gerenciarMissoes |
| `ResponsavelHemocentro` | confirmarDoacao, atualizarLocal, gerenciarCampanhas |

Na confirmação de uma doação, o `DoacaoService` não pergunta qual é o tipo do usuário. Ele chama `possuiPermissao("confirmarDoacao")`, e a resposta depende da subclasse real do objeto. Um doador recebe erro 409; um administrador ou responsável consegue confirmar.

### Polimorfismo 2: interface `RequisitoDesbloqueio`

Cada visual do mascote tem um requisito para ser liberado. O texto fica na coluna `item.requisito_desbloqueio`, por exemplo `Nível Doador`, `Missão Conhecer os requisitos`, `Conquista Primeira Doação` ou `Evento Mês do Doador (exemplo)`.

A interface `RequisitoDesbloqueio` define dois métodos, `estaCumprido()` e `descricao()`, e tem cinco implementações:

| Classe | Confere se o usuário... |
|---|---|
| `RequisitoNivel` | já alcançou o nível exigido |
| `RequisitoMissao` | concluiu a missão exigida |
| `RequisitoConquista` | desbloqueou a conquista exigida |
| `RequisitoEvento` | está dentro do período do evento |
| `SemRequisito` | (sempre liberado) |

O `MascoteService` só chama `requisito.estaCumprido(progresso)`, e cada classe responde do seu jeito. Para criar um novo tipo de requisito, basta criar outra classe que implemente a interface, sem alterar o service.

### Encapsulamento

As regras importantes ficam dentro das próprias entidades, e os atributos não podem ser alterados livremente:

- `Usuario` **não tem** `setPontuacao`. A pontuação só aumenta pelo método `adicionarPontos()`, que recusa valores negativos ou zero.
- `Doacao.confirmar()` recusa uma segunda confirmação da mesma doação.
- A senha é guardada como hash BCrypt e nunca aparece nas respostas da API.

---

## 🗄️ Banco de dados

O banco `hemoplay` tem 16 tabelas: `tipo_sanguineo`, `nivel`, `local_doacao`, `evento`, `conquista`, `item`, `usuario`, `doacao`, `campanha`, `participacao_campanha`, `necessidade_sanguinea`, `missao`, `participacao_missao`, `usuario_conquista`, `inventario` e `notificacao`.

Os scripts ficam na pasta `database/` e devem ser executados **nesta ordem**:

| Ordem | Script | O que faz |
|---|---|---|
| 1 | `schema.sql` | Apaga o banco `hemoplay` (se existir) e cria tudo do zero |
| 2 | `seed.sql` | Insere os 8 tipos sanguíneos e os 5 níveis |
| 3 | `dados-teste.sql` | Insere 4 hemocentros, 1 evento, 2 campanhas, estoques simulados, 15 missões, 5 conquistas, 14 visuais do mascote, 3 usuários de teste e notificações de exemplo |

**Usuários de teste criados pelo `dados-teste.sql`:**

| ID | Tipo | Uso nos testes |
|---|---|---|
| 1 | Doador | Doador de exemplo, já com pontos e notificações |
| 2 | Administrador | Pode confirmar doações |
| 3 | Responsável pelo hemocentro | Pode confirmar doações e ver o painel de pendentes |

As tabelas são criadas pelos scripts SQL, e não pelo Hibernate. A configuração `spring.jpa.hibernate.ddl-auto=validate` faz o Spring conferir, ao iniciar, se cada entidade Java corresponde à tabela do banco. Se algo estiver diferente, o sistema nem sobe, o que evita erros escondidos.

> ⚠️ O `schema.sql` começa apagando o banco inteiro. Use apenas em ambiente de desenvolvimento.

---

## 🚀 Como preparar o ambiente e rodar o projeto

### 1. Programas necessários

| Programa | Onde conseguir | Observação |
|---|---|---|
| **JDK 21** | [Eclipse Temurin (Adoptium)](https://adoptium.net/) | No IntelliJ, também dá para baixar em *File → Project Structure → SDK → Download JDK*. |
| **MySQL Server 8.0** e **MySQL Workbench** | [MySQL Installer](https://dev.mysql.com/downloads/installer/) | Na instalação, anote a senha definida para o usuário `root`. |
| **IntelliJ IDEA** ou **VS Code** | [IntelliJ Community](https://www.jetbrains.com/idea/download/) / [VS Code](https://code.visualstudio.com/) | No VS Code, instale as extensões **Extension Pack for Java** e **Spring Boot Extension Pack**. |
| **Postman** | [postman.com/downloads](https://www.postman.com/downloads/) | Para testar a API. |
| Navegador | — | Para usar a interface gráfica. |

Não é preciso instalar o Maven: o projeto já traz o Maven Wrapper, e as duas IDEs o reconhecem sozinhas.

### 2. Baixar o projeto

1. Abra o repositório no GitHub: <https://github.com/Gu-Ichikawa/AEP-4-Semestre>.
2. Clique no botão verde **Code** e depois em **Download ZIP** (ou em **Open with GitHub Desktop**, se preferir clonar).
3. Extraia o ZIP em uma pasta de sua escolha.

### 3. Criar o banco de dados no MySQL Workbench

1. Abra o **MySQL Workbench** e entre na conexão local (normalmente *Local instance MySQL80*), usando a senha do `root`.
2. Vá em **File → Open SQL Script...** e abra `database/schema.sql`.
3. Clique no ícone de **raio** (Execute) para rodar o script inteiro.
4. Repita os passos 2 e 3 com `database/seed.sql` e depois com `database/dados-teste.sql`.
5. No painel **Schemas**, à esquerda, clique no ícone de atualizar. O banco `hemoplay` deve aparecer com as 16 tabelas.

> Para começar os testes do zero a qualquer momento, basta rodar os três scripts de novo, na mesma ordem.

### 4. Configurar a senha do banco

A senha do MySQL **não fica no repositório**: o arquivo `application.properties` está no `.gitignore`. Por isso, é preciso criá-lo a partir do modelo:

1. Na IDE, abra a pasta `src/main/resources`.
2. Copie o arquivo `application.properties.example` e cole na mesma pasta.
3. Renomeie a cópia para `application.properties`.
4. Abra o arquivo e troque `SUA_SENHA_AQUI` pela senha do `root` do seu MySQL. Se o seu usuário não for `root`, troque também o `username`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hemoplay
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
spring.jpa.hibernate.ddl-auto=validate
```

### 5. Rodar o sistema pela IDE

**No IntelliJ IDEA**

1. Vá em **File → Open** e selecione a pasta do projeto (a que tem o `pom.xml`). Se aparecer a pergunta, clique em **Trust Project**.
2. Aguarde o IntelliJ terminar de baixar as dependências (barra de progresso no canto inferior direito).
3. Abra `src/main/java/com/hemoplay/HemoplayApplication.java`.
4. Clique no **triângulo verde** ao lado da linha `public class HemoplayApplication` e escolha **Run**.
5. Para parar, clique no **quadrado vermelho** da aba *Run*.

**No VS Code**

1. Vá em **File → Open Folder** e selecione a pasta do projeto.
2. Aguarde aparecer **Java: Ready** na barra inferior. Na primeira vez, as dependências são baixadas e isso pode levar alguns minutos.
3. Abra `src/main/java/com/hemoplay/HemoplayApplication.java` e clique em **Run**, que aparece logo acima do método `main`. Também dá para usar o painel **Spring Boot Dashboard**, na barra lateral, e clicar no botão de play ao lado de `hemoplay`.
4. Para parar, clique no **quadrado vermelho** da barra de depuração.

O sistema está no ar quando o console mostrar uma linha parecida com:

```
Started HemoplayApplication in 4.2 seconds
```

A partir daí, a interface fica em <http://localhost:8080> e a API responde no mesmo endereço.

### 6. Problemas comuns

| Mensagem ou sintoma | Causa provável | Como resolver |
|---|---|---|
| `Failed to determine a suitable driver class` | O arquivo `application.properties` não existe | Refaça o [passo 4](#4-configurar-a-senha-do-banco). |
| `Access denied for user 'root'@'localhost'` | Senha errada no `application.properties` | Confira a senha do MySQL no arquivo. |
| `Unknown database 'hemoplay'` | Os scripts não foram executados | Refaça o [passo 3](#3-criar-o-banco-de-dados-no-mysql-workbench). |
| `Schema-validation: missing table` ou `missing column` | Banco criado com uma versão antiga dos scripts | Rode de novo os três scripts, na ordem. |
| `Port 8080 was already in use` | O sistema já está rodando em outra janela ou IDE | Pare a outra execução e rode de novo. |
| Página *Whitelabel Error Page* (404) em `localhost:8080` | Arquivos compilados antigos na pasta `target` | No IntelliJ: painel **Maven** (lado direito) → *hemoplay* → *Lifecycle* → clique duas vezes em **clean**. No VS Code: painel **Maven** (barra lateral) → clique com o botão direito em *hemoplay* → **clean**. Depois, rode o sistema de novo. |
| Missões aparecem como "ENCERRADA" | Os dados de teste usam datas de nov. a dez. de 2026 | Ajuste as colunas `data_inicio` e `data_fim` das tabelas `missao` e `evento` no `dados-teste.sql` e rode os scripts de novo. |

---

## 🖥️ Interface gráfica

Fizemos uma interface gráfica para que o sistema fique **mais fácil de visualizar e de testar**, já que nela dá para ver o mascote, os níveis e as notificações acontecendo de forma visual. Mas a interface **não é o foco deste projeto**: o foco é o back-end (orientação a objetos, regras de negócio e CRUD) e o banco de dados. Por isso, também deixamos abaixo o [passo a passo de testes com o Postman](#-testando-a-api-com-o-postman), que mostra diretamente o funcionamento da API e a persistência no banco.

A interface é feita com HTML, CSS e JavaScript puros, fica em `src/main/resources/static` e é servida pelo próprio Spring Boot. Ela não tem regras próprias: todas as telas consomem os mesmos endpoints testados no Postman. As ilustrações do mascote foram feitas pela equipe.

### Como usar

Com o sistema rodando, abra <http://localhost:8080> no navegador.

| Tela | O que mostra |
|---|---|
| **Entrada** | Cadastro de um novo doador, ou "entrar com ID" usando um dos usuários de teste (1, 2 ou 3) |
| **Início** | Mascote, nível, barra de progresso, pontos, evento ativo, estoques críticos e próximas missões |
| **Doações** | Registro de doação, histórico e data a partir da qual a pessoa pode doar de novo |
| **Missões** | Missões agrupadas por categoria e conquistas desbloqueadas ou bloqueadas |
| **Mascote** | Cores e acessórios. Os visuais bloqueados aparecem em cinza, com o requisito para liberar |
| **Hemocentros** | Estoque simulado dos 8 tipos sanguíneos, campanhas e locais de doação |
| **Como doar** | Requisitos para doar e intervalo entre doações |
| **Perfil** | Consulta, edição e exclusão da conta, e lista de permissões do usuário |
| **Painel** | Doações aguardando confirmação (aparece só para administrador e responsável pelo hemocentro) |

O sino no topo mostra as notificações. Quando o usuário sobe de nível, aparece uma comemoração na tela.

### Roteiro sugerido para a demonstração

1. Crie uma conta escolhendo um tipo sanguíneo. A missão *Completar o cadastro* e a conquista *Primeiro Passo* são concedidas na hora.
2. Abra **Como doar**. As missões de leitura são concluídas.
3. Em **Doações**, registre uma doação. Em novembro de 2026, o evento *Mês do Doador* também conta, e o usuário sobe para o nível Apoiador.
4. Em **Mascote**, equipe um dos visuais que foram liberados.
5. Saia e entre com o **ID 3** (responsável pelo hemocentro). Em **Painel**, confirme a doação.
6. Saia e entre de novo com a sua conta: veja os pontos, o novo nível, as conquistas e as notificações.

---

## 📮 Testando a API com o Postman

### Como montar uma requisição no Postman

1. Clique em **New → HTTP Request** (ou no **+** ao lado das abas).
2. Escolha o método (GET, POST, PUT, PATCH ou DELETE) na caixa à esquerda da URL.
3. Digite a URL, por exemplo `http://localhost:8080/locais`.
4. Para requisições com corpo (POST, PUT e PATCH), vá na aba **Body**, marque **raw** e escolha **JSON** na lista à direita. Cole o JSON do teste.
5. Clique em **Send**. O resultado e o código de status (200, 201, 400...) aparecem na parte de baixo.

Dica: crie uma **Collection** (*New → Collection*) e salve cada requisição nela (`Ctrl + S`) para repetir os testes depois.

### Endpoints disponíveis

| Método | URL | O que faz | RF |
|---|---|---|---|
| POST | `/usuarios` | Cadastra um doador | RF01 |
| GET | `/usuarios/{id}` | Consulta um usuário | RF02 |
| PUT | `/usuarios/{id}` | Atualiza nome, telefone, data de nascimento e tipo sanguíneo | RF02 |
| DELETE | `/usuarios/{id}` | Exclui o usuário (e, em cascata, as doações dele) | CRUD |
| GET | `/tipos-sanguineos` | Lista os tipos sanguíneos | RF02 |
| GET | `/locais` | Lista os locais de doação | RF04 |
| GET | `/campanhas` | Lista as campanhas | RF04 |
| GET | `/necessidades` | Lista o estoque simulado de cada tipo sanguíneo | RF11 |
| GET | `/eventos/ativos` | Lista os eventos sazonais em andamento | RF14 |
| POST | `/doacoes` | Registra uma doação (status REGISTRADA) | RF05 |
| PATCH | `/doacoes/{id}/confirmar` | Confirma uma doação (status CONFIRMADA) | RF05 |
| GET | `/doacoes/pendentes?idUsuario={id}` | Lista doações aguardando confirmação (só para quem pode confirmar) | RF12 |
| GET | `/usuarios/{id}/doacoes` | Histórico de doações do usuário | RF06 |
| GET | `/usuarios/{id}/missoes` | Missões com a situação de cada uma para o usuário | RF07 |
| POST | `/usuarios/{id}/missoes/{idMissao}/concluir` | Conclui uma missão de visita (ler requisitos, ver locais...) | RF07 |
| GET | `/usuarios/{id}/conquistas` | Conquistas desbloqueadas e bloqueadas | RF09 |
| GET | `/usuarios/{id}/mascote` | Visual atual e lista de visuais com o status de cada um | RF10 |
| PUT | `/usuarios/{id}/mascote` | Equipa um visual desbloqueado | RF10 |
| GET | `/usuarios/{id}/notificacoes` | Notificações do usuário | RF13 |
| PATCH | `/usuarios/{id}/notificacoes/lidas` | Marca todas as notificações como lidas | RF13 |

**Códigos de resposta:**

| Código | Quando acontece |
|---|---|
| `200 OK` | Consulta ou alteração feita |
| `201 Created` | Cadastro feito (usuário ou doação) |
| `204 No Content` | Exclusão feita ou notificações marcadas como lidas |
| `400 Bad Request` | Dado inválido ou não encontrado (e-mail inválido, usuário inexistente...) |
| `409 Conflict` | Regra de negócio violada (doação já confirmada, usuário sem permissão, visual bloqueado...) |

Os erros sempre voltam no formato `{ "erro": "mensagem explicando o problema" }`.

### Roteiro de testes

Rode os três scripts do banco antes de começar, para que os IDs fiquem iguais aos do roteiro. Se o banco já tiver sido usado, use os IDs que aparecem nas respostas.

Os pontos indicados consideram que os testes acontecem durante o evento *Mês do Doador* (novembro de 2026). Fora dele, a missão do evento não é concedida e os valores ficam menores.

#### Teste 1: cadastrar um doador (Create)

**POST** `http://localhost:8080/usuarios`

```json
{
  "nome": "Maria Silva",
  "email": "maria@exemplo.com",
  "senha": "senha123",
  "dataNasc": "1998-03-15",
  "telefone": "(44) 99999-0000",
  "idTipoSanguineo": 8
}
```

Resultado esperado: **201 Created**, com `"id": 4`, `"tipo": "Doador"`, `"tipoSanguineo": "O-"`, `"pontuacao": 20` e `"nivel": "Iniciante"`. A senha não aparece na resposta. Os 20 pontos vêm da missão *Completar o cadastro*.

#### Teste 2: validação e e-mail repetido

Envie o mesmo JSON do teste 1 de novo. Resultado: **400**, com `"erro": "E-mail já cadastrado"`.

Agora envie `"email": "maria"` e `"senha": "123"`. Resultado: **400**, com as mensagens de e-mail inválido e de tamanho da senha.

#### Teste 3: consultar o usuário (Read)

**GET** `http://localhost:8080/usuarios/4`

Resultado: **200 OK**, com os dados da Maria e a lista `permissoes` do doador.

#### Teste 4: atualizar o perfil (Update)

**PUT** `http://localhost:8080/usuarios/4`

```json
{
  "nome": "Maria Silva Souza",
  "telefone": "(44) 98888-1111",
  "dataNasc": "1998-03-15",
  "idTipoSanguineo": 8
}
```

Resultado: **200 OK**, com o nome e o telefone novos.

#### Teste 5: consultas gerais

Envie um **GET** para cada URL abaixo. Todas devem responder **200 OK** com listas:

- `http://localhost:8080/tipos-sanguineos`
- `http://localhost:8080/locais`
- `http://localhost:8080/campanhas`
- `http://localhost:8080/necessidades` (o tipo O- aparece como `CRITICO`)
- `http://localhost:8080/eventos/ativos`

#### Teste 6: registrar uma doação

**POST** `http://localhost:8080/doacoes`

```json
{
  "idUsuario": 4,
  "idLocal": 1
}
```

Resultado: **201 Created**, com `"id": 1` e `"status": "REGISTRADA"`. Ao consultar a Maria de novo (teste 3), ela tem 130 pontos e nível *Apoiador*: 50 pontos da missão *Registrar uma doação* e 60 da missão do evento.

#### Teste 7: doador tentando confirmar a própria doação

**PATCH** `http://localhost:8080/doacoes/1/confirmar`

```json
{
  "idUsuarioConfirmacao": 4
}
```

Resultado: **409 Conflict**, com `"erro": "Este usuário não tem permissão para confirmar doações"`. Esse é o polimorfismo de `obterPermissoes()` em ação.

#### Teste 8: painel de doações pendentes

**GET** `http://localhost:8080/doacoes/pendentes?idUsuario=3`

Resultado: **200 OK**, com a doação 1 na lista. Com `idUsuario=4` (um doador), o resultado é **409**.

#### Teste 9: confirmar a doação

**PATCH** `http://localhost:8080/doacoes/1/confirmar`

```json
{
  "idUsuarioConfirmacao": 3
}
```

Resultado: **200 OK**, com `"status": "CONFIRMADA"` e a `dataConfirmacao` preenchida. A Maria passa a ter 330 pontos e o nível *Doador*: 100 pontos pela doação confirmada e 100 pela missão *Doação confirmada*.

Envie a mesma requisição de novo. Resultado: **409**, com `"erro": "Esta doação já foi confirmada."`

#### Teste 10: histórico, missões, conquistas e notificações

- **GET** `http://localhost:8080/usuarios/4/doacoes`: a doação aparece como `CONFIRMADA`.
- **GET** `http://localhost:8080/usuarios/4/missoes`: as missões concluídas aparecem com `"situacao": "CONCLUIDA"`.
- **GET** `http://localhost:8080/usuarios/4/conquistas`: *Primeiro Passo* e *Primeira Doação* aparecem com `"desbloqueada": true`.
- **GET** `http://localhost:8080/usuarios/4/notificacoes`: avisos de missões, conquistas, pontos e mudanças de nível.
- **PATCH** `http://localhost:8080/usuarios/4/notificacoes/lidas` (sem corpo): **204 No Content**. Consulte as notificações de novo e todas estarão com `"lida": true`.

#### Teste 11: missões de visita

**POST** `http://localhost:8080/usuarios/4/missoes/2/concluir` (sem corpo)

Resultado: **200 OK**, com a missão *Conhecer os requisitos* concluída e mais 10 pontos para o usuário.

Agora tente a missão 1: **POST** `http://localhost:8080/usuarios/4/missoes/1/concluir`. Resultado: **409**, porque *Completar o cadastro* é concluída automaticamente pelo sistema e não pode ser marcada à mão.

#### Teste 12: mascote

**GET** `http://localhost:8080/usuarios/4/mascote`

Resultado: **200 OK**, com o visual `atual` e a lista `visuais`. Cada item informa se está `desbloqueado` e qual é o `requisito`.

**PUT** `http://localhost:8080/usuarios/4/mascote`

```json
{
  "idItem": 6
}
```

Resultado: **200 OK**. Os *Óculos redondos* ficam equipados, porque a missão *Conhecer os requisitos* foi concluída no teste 11. Como é um acessório, o usuário também ganha a missão *Personalizar o mascote* e a conquista *Mascote Vestido*.

Agora tente `{ "idItem": 14 }`. Resultado: **409**, com `"erro": "Visual bloqueado. Alcance o nível Herói da Vida."` Esse é o polimorfismo de `RequisitoDesbloqueio` em ação.

#### Teste 13: excluir o usuário (Delete)

**DELETE** `http://localhost:8080/usuarios/4`

Resultado: **204 No Content**. Consulte o usuário de novo (teste 3) e o resultado será **400**, com `"erro": "Usuário não encontrado"`.

### Conferindo a persistência no MySQL Workbench

Durante os testes, dá para conferir no Workbench que os dados foram gravados de verdade. Abra uma aba de consulta (**File → New Query Tab**), cole a consulta e clique no raio:

```sql
USE hemoplay;

-- Usuários, pontos e nível (a senha aparece criptografada com BCrypt)
SELECT id_usuario, tipo_usuario, nome, senha, pontuacao, id_nivel, mascote_cor, mascote_acessorios
FROM usuario;

-- Doações e status
SELECT * FROM doacao;

-- Missões concluídas por usuário
SELECT pm.id_usuario, m.nome, pm.data_conclusao
FROM participacao_missao pm
JOIN missao m ON m.id_missao = pm.id_missao;

-- Conquistas desbloqueadas
SELECT uc.id_usuario, c.nome, uc.data_desbloqueio
FROM usuario_conquista uc
JOIN conquista c ON c.id_conquista = uc.id_conquista;

-- Visual equipado no inventário
SELECT * FROM inventario;

-- Notificações geradas
SELECT id_usuario, tipo, titulo, lida FROM notificacao ORDER BY data_envio DESC;
```

Depois do teste 13, a Maria e todas as linhas ligadas a ela (doações, missões, conquistas, inventário e notificações) somem das tabelas, por causa do `ON DELETE CASCADE`.

---

## 🎮 Regras de gamificação

### Pontos e níveis (RF08)

O usuário ganha pontos por doação confirmada (100 pontos) e pelas missões concluídas (cada missão tem a sua pontuação). Depois de cada ganho, o nível é recalculado:

| Nível | Pontuação |
|---|---|
| Iniciante | 0 a 99 |
| Apoiador | 100 a 299 |
| Doador | 300 a 599 |
| Guardião da Vida | 600 a 999 |
| Herói da Vida | 1000 ou mais |

Uma missão ou conquista só pode ser recebida **uma vez** por usuário. Isso é garantido no código e também no banco, por chaves `UNIQUE` e pela chave primária composta.

### Missões concluídas automaticamente (RF07)

| Missão | Quando é concluída |
|---|---|
| Completar o cadastro | Ao cadastrar ou atualizar o perfil com tipo sanguíneo |
| Registrar uma doação | Ao registrar uma doação |
| Doação do mês do doador | Ao registrar uma doação durante o evento |
| Doação confirmada | Quando o hemocentro confirma uma doação |
| Três doações no ano | Ao ter 3 doações confirmadas no mesmo ano |
| Dupla doação do mês | Ao ter 2 doações confirmadas durante o evento |
| Personalizar o mascote | Ao equipar qualquer visual |
| Conhecer os requisitos, Conhecer o intervalo, Visitar um local e Ver seu histórico | Ao abrir a página correspondente (pela interface ou pelo endpoint `/concluir`) |

### Conquistas (RF09)

| Conquista | Critério | Recompensa |
|---|---|---|
| Primeiro Passo | Cadastro com tipo sanguíneo | Laço vermelho |
| Primeira Doação | 1 doação confirmada | Colar de rubi |
| Doador Constante | 3 doações confirmadas | Cor azul-noite e Pulseira de contagem |
| Mascote Vestido | Equipar um acessório | Gravata borboleta |
| Herói do Estoque | Doar em campanha de estoque crítico | (ainda não automatizada) |

### Mascote (RF10)

São 14 visuais: 4 cores e 10 acessórios. O usuário equipa **um visual por vez**: ou uma cor, ou um acessório (os acessórios aparecem sobre o mascote vermelho). Cada visual tem um requisito de nível, missão, conquista ou evento, conferido pela interface `RequisitoDesbloqueio`.

### Notificações (RF13)

O sistema cria notificações automaticamente ao concluir missões, desbloquear conquistas, ganhar pontos e subir de nível.

### Intervalo entre doações

Seguindo os critérios do Ministério da Saúde, a interface mostra quando o usuário poderá doar de novo: 60 dias depois da última doação para homens e 90 dias para mulheres. As missões e conquistas foram pensadas para não premiar doações antecipadas: as de repetição dependem de doações **confirmadas** pelo hemocentro.

---

## 📝 Ajustes em relação à documentação da 1ª entrega

Durante o desenvolvimento, alguns pontos do documento, do DER e do diagrama de classes da 1ª entrega precisaram ser ajustados. Os arquivos originais continuam na pasta `docs/` sem alterações. As mudanças estão registradas abaixo.

### Diagrama de classes

| Classe | Como estava na 1ª entrega | Como ficou | Motivo |
|---|---|---|---|
| `Usuario` | `obterPermissoes() : String` | `obterPermissoes() : List<String>` e o novo método `possuiPermissao(String)` | Um usuário tem várias permissões. Com a lista, o service pergunta "pode fazer X?" sem verificar o tipo do usuário. |
| `Usuario` | Sem pontuação nem mascote | Atributos `pontuacao`, `mascoteCor` e `mascoteAcessorios`, além do método `adicionarPontos()` | Esses campos já estavam no DER e eram necessários para o RF08 e o RF10. |
| `Usuario` | Métodos `autenticar()`, `atualizarPerfil()` e `consultarProgresso()` | `atualizarPerfil` e a consulta de progresso foram para o `UsuarioService` e o `GamificacaoService`. `autenticar()` não foi implementado. | Pela arquitetura em camadas, as regras ficam nos services. O login ficou fora do escopo (veja [Limitações](#️-limitações-conhecidas)). |
| `Doador`, `Administrador`, `ResponsavelHemocentro` | Métodos como `registrarDoacao()`, `gerenciarCampanhas()` e `atualizarInformacoesLocal()` | Viraram **permissões** retornadas por `obterPermissoes()`. As ações estão nos services. | Assim as subclasses descrevem o que cada usuário pode fazer, e o polimorfismo fica concentrado em um único método. |
| `Nivel` | `pontosMinimos` e `pontosMaximos` | `pontuacaoMin` e `pontuacaoMax`. O máximo pode ser nulo no último nível. | Mesmos nomes do DER e do banco. |
| `TipoSanguineo` | `nome` e `descricao` | `tipo` e `fatorRh` (ex.: `O` e `-`), e o método `getRotulo()` que devolve `O-` | Segue o DER e permite validar os valores no banco com `CHECK`. |
| `NecessidadeSanguinea` | `status` e `dataAtualizacao` | Também tem `quantidadeSimulada` e a ligação com `TipoSanguineo` | Necessário para mostrar o estoque simulado de cada tipo (RF11). |
| `Campanha` | Sem local, status ou tipo sanguíneo | Atributos `status`, `local` e `tipoSanguineo` (opcional) | Cada campanha acontece em um local, e uma campanha pode ser ligada a um tipo sanguíneo em estoque crítico (RF11). |
| `LocalDoacao` | `horarioAtendimento` | `horarioFuncionamento`, e o novo atributo `estado` | Mesmos nomes do DER e do banco. |
| `Doacao` | `status` como texto e o método `confirmarDoacao()` | `status` é o enum `StatusDoacao` (REGISTRADA ou CONFIRMADA). O método virou `confirmar()` e recusa uma segunda confirmação. A doação também guarda o local. | O enum impede status inválidos e a regra fica protegida dentro da entidade. |
| `Missao` | `pontosRecompensa` e o método `verificarCriterios()` | `pontuacao`, `ativa` e a ligação com `Evento`. A verificação dos critérios ficou no `GamificacaoService`. | A ligação com o evento permite missões sazonais (RF14). Os critérios dependem de dados de outras tabelas, então ficaram no service. |
| `ParticipacaoMissao` | Método `concluirMissao()` | O próprio construtor cria a participação já concluída, com o campo `recompensaRecebida` | Uma missão só é registrada quando é concluída, o que evita registros pela metade. |
| `UsuarioConquista` | `dataConquista` | `dataDesbloqueio`, com chave composta (classe `UsuarioConquistaId`) | Mesmo nome do DER. A chave composta impede que a mesma conquista seja dada duas vezes. |
| `Conquista` | Método `verificarCriterio()` | A verificação ficou no `GamificacaoService` | Mesmo motivo das missões. |
| `Inventario` | `quantidade` e `dataDesbloqueio` | Também tem `equipado`, e os métodos `equipar()` e `desequipar()` | Para saber qual visual o mascote está usando. |
| `Item` | `requisitoDesbloqueio` como texto | O texto é interpretado pela nova interface `RequisitoDesbloqueio` | Permite conferir automaticamente se o visual está liberado (segundo exemplo de polimorfismo). |
| *(nova)* | — | Interface `RequisitoDesbloqueio`, classes `RequisitoNivel`, `RequisitoMissao`, `RequisitoConquista`, `RequisitoEvento`, `SemRequisito` e o record `ProgressoUsuario` | Regras de desbloqueio dos visuais do mascote. |
| *(nova)* | — | Enum `StatusDoacao` | Status da doação. |
| `ParticipacaoCampanha` | Classe prevista | A tabela existe no banco, mas a classe Java não foi criada nesta entrega | A participação em campanhas ficou para uma próxima etapa. |

### DER e banco de dados

- **Descrição em `local_doacao`:** foi criada a coluna `descricao`, para mostrar mais informações sobre cada local.
- **Campanha ligada a um local e a um tipo sanguíneo:** cada campanha tem `id_local` e, opcionalmente, `id_tipo_sanguineo`. A equipe escolheu essa modelagem para destacar as campanhas de tipos em estoque crítico.
- **Tipo do usuário:** a tabela `usuario` ganhou a coluna `tipo_usuario` (DOADOR, ADMINISTRADOR ou RESPONSAVEL_HEMOCENTRO), usada pela herança `SINGLE_TABLE`.
- **Exclusão em cascata:** ao excluir um usuário, as doações, participações, conquistas, inventário e notificações dele também são excluídos (`ON DELETE CASCADE`). A equipe discutiu manter as doações de usuários excluídos e decidiu pela exclusão em cascata.
- **Restrições:** foram incluídas regras `CHECK` (tipos e fatores sanguíneos válidos, pontuação não negativa, datas de fim depois das de início, status de doação coerente com a data de confirmação) e `UNIQUE` (e-mail, nome de nível, nome de conquista e nome de item).
- **Ordem dos scripts:** a criação do banco foi separada em `schema.sql` (estrutura), `seed.sql` (dados fixos) e `dados-teste.sql` (exemplos).

### Documento da 1ª entrega

- **Confirmação de doações (RF05):** o documento não dizia quem confirma. Ficou definido que só o **Administrador** e o **Responsável pelo hemocentro** podem confirmar, e só a doação confirmada gera os pontos da doação.
- **Eventos sazonais (RF14):** o documento citava Halloween e Natal como exemplos. Os dados de teste usam o evento **Mês do Doador** (novembro de 2026), com duas missões e um visual exclusivo.
- **Conquistas:** os exemplos *Conhecedor da Causa*, *Doador Frequente* e *Guardião da Vida* foram substituídos por *Doador Constante*, *Herói do Estoque* e *Mascote Vestido*. *Primeiro Passo* e *Primeira Doação* foram mantidas. *Guardião da Vida* continua existindo como nível.
- **Mascote (RF10):** a evolução do mascote foi implementada com **visuais desbloqueáveis** (cores e acessórios) ligados a níveis, missões, conquistas e eventos. O usuário equipa um visual por vez.
- **Camada View:** a interface foi feita com páginas estáticas (HTML, CSS e JavaScript) servidas pelo Spring Boot, que consomem a mesma API REST testada no Postman.
- **Tecnologias:** foram acrescentados o Spring Security Crypto (para criptografar as senhas com BCrypt) e o Jakarta Validation (para validar os dados recebidos).

---

## ⚠️ Limitações conhecidas

- **Sem login:** a autenticação ficou fora do escopo desta entrega. Na API, o usuário é identificado pelo ID na URL ou no JSON, e na interface pela opção "entrar com ID". As senhas já são gravadas com BCrypt, prontas para um login futuro.
- **Administração parcial (RF12):** o administrador e o responsável já confirmam doações e veem as pendentes. O cadastro de campanhas, missões, locais e itens ainda é feito pelos scripts SQL, sem telas ou endpoints próprios.
- **Missões ainda não automatizadas:** *Mantenha o ritmo*, *Doar em campanha*, *Doar pelo estoque crítico* e *Convide um amigo* estão no banco e aparecem como "Em breve" na interface. A conquista *Herói do Estoque* também depende delas.
- **Participação em campanhas:** a tabela `participacao_campanha` existe, mas ainda não é usada pela API.
- **Intervalo entre doações:** a próxima data possível é informada na interface, mas o back-end ainda não bloqueia um novo registro dentro do intervalo.
- **Datas dos dados de teste:** as missões e o evento estão cadastrados para novembro e dezembro de 2026. Para testar em outra data, ajuste as datas no `dados-teste.sql`.

---

## 📋 Requisitos funcionais

| Requisito | Situação | Onde |
|---|---|---|
| **RF01 — Cadastro de usuário** | ✅ Atendido | `POST /usuarios` |
| **RF02 — Gerenciamento do perfil** | ✅ Atendido | `GET` e `PUT /usuarios/{id}`, tipo sanguíneo opcional |
| **RF03 — Consulta de informações** | ✅ Atendido | Tela *Como doar* |
| **RF04 — Consulta de locais e campanhas** | ✅ Atendido | `GET /locais` e `GET /campanhas` |
| **RF05 — Registro de doação** | ✅ Atendido | `POST /doacoes` e `PATCH /doacoes/{id}/confirmar` |
| **RF06 — Histórico de participação** | ✅ Atendido | `GET /usuarios/{id}/doacoes`, missões e conquistas |
| **RF07 — Missões** | ✅ Atendido | 11 das 15 missões concluídas automaticamente |
| **RF08 — Pontuação e progressão** | ✅ Atendido | `GamificacaoService`, 5 níveis |
| **RF09 — Conquistas** | ✅ Atendido | 4 das 5 conquistas automatizadas |
| **RF10 — Mascote** | ✅ Atendido | `GET` e `PUT /usuarios/{id}/mascote`, 14 visuais |
| **RF11 — Necessidades simuladas** | ✅ Atendido | `GET /necessidades`, campanha ligada ao tipo crítico |
| **RF12 — Administração** | 🟡 Parcial | Confirmação e painel de pendentes; demais cadastros via SQL |
| **RF13 — Notificações** | ✅ Atendido | Geradas automaticamente, `GET` e `PATCH` de notificações |
| **RF14 — Eventos sazonais** | ✅ Atendido | Evento *Mês do Doador*, missões e visual exclusivos |

---

## 📅 Cronograma

### 1ª entrega: concepção e documentação

| Data | Atividade | Responsável |
|---|---|---|
| 01/09/2026 | Levantamento do problema e principais dores | João e Gustavo |
| 02/09/2026 | Partes interessadas e valores do sistema | João |
| 03/09/2026 | Regras de negócio | Gustavo |
| 04/09/2026 | Escopo e concepção do sistema | João e Gustavo |
| 05/09/2026 | Gamificação e jornada do usuário | João |
| 06/09/2026 | Levantamento e definição dos requisitos | João e Gustavo |
| 07/09/2026 | Arquitetura e tecnologias | Gustavo |
| 08/09/2026 | Diagrama de classes | Gustavo |
| 09/09/2026 | DER e estrutura do banco de dados | João |
| 10/09/2026 | Estrutura do GitHub e README | Gustavo |
| 11/09/2026 | Revisão e integração da documentação | João e Gustavo |
| 12/09/2026 | Revisão final e preparação da entrega | João e Gustavo |

### 2ª entrega: implementação

| Data | Atividade | Responsável |
|---|---|---|
| 21/10/2026 | Criação do projeto Spring Boot e das dependências | João e Gustavo |
| 21/10/2026 a 08/11/2026 | Criação das tabelas no `schema.sql` | Gustavo |
| 08/11/2026 | Dados de teste e `seed.sql` | João e Gustavo |
| 08/11/2026 | Organização dos pacotes e conexão com o MySQL | Gustavo |
| 11/11/2026 | Models, repositories, services, controllers e DTOs | João |
| 11/11/2026 | Revisão dos models e regra de confirmação de doações | Gustavo |
| 12/11/2026 a 13/11/2026 | Gamificação, mascote, interface gráfica e README | João e Gustavo |

---

## 📚 Documentação de apoio

- [Documento da 1ª entrega (PDF)](docs/AEP%204%20Semestre%20Jo%C3%A3o%20Marcos%20Gustavo%20Ichikawa.pdf)
- [DER (PDF)](docs/DER%20-%20HemoPlay.pdf) | [versão online no BRModelo](https://app.brmodeloweb.com/publicview/6aa4a32de01c82da4dab49fd)
- [Diagrama de classes (PDF)](docs/Diagrama%20de%20Classe%20-%20HemoPlay.pdf) | [versão online no Lucidchart](https://lucid.app/lucidchart/ae70b090-da4e-40da-a51b-28309bb4d391/view)
- Repositório: <https://github.com/Gu-Ichikawa/AEP-4-Semestre>
