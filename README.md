# Big Bank AP2

## 📝 Descrição

**Big Bank AP2** é um sistema de software desktop para gestão de informações bancárias, desenvolvido como projeto final para a disciplina de Algoritmo e Programação 2. A aplicação permite a administração centralizada e eficiente de dados de clientes, contas e produtos financeiros, como cartões de crédito, empréstimos e investimentos.

O sistema foi construído em Java, com interface gráfica utilizando JavaFX e persistência de dados em arquivos locais, simulando o ambiente operacional de uma agência bancária.

Este projeto foi desenvolvido por:
* Victor Samir Ribeiro dos Anjos
* João Kassio Ferreira Santos Muniz
* Pedro Damasceno do Prado Cabral
* Willian Rulian Ferreira dos Anjos

Para a disciplina de Algoritmo e Programação 2, lecionada pela Profª. Maísa Soares dos Santos Lopes, na Universidade Estadual do Sudoeste da Bahia (UESB).

---

## ✨ Funcionalidades Principais

O sistema oferece uma gama de funcionalidades para a gestão bancária:

* **Autenticação de Usuário**:
    * Tela de login para acesso ao sistema (credenciais fixas: `admin`/`admin`).

* **Gestão de Clientes (CRUD Completo)**:
    * Adicionar, editar e visualizar clientes com dados como nome, CPF, data de nascimento, etc.
    * Validação de CPF para evitar duplicidade e garantir formato válido.
    * Exclusão de clientes em cascata: ao remover um cliente, todas as suas contas e produtos associados são excluídos automaticamente.

* **Gestão de Contas Bancárias**:
    * Criação de **Conta Corrente** e **Conta Poupança** para clientes cadastrados.
    * Ações de depósito e saque, com regras específicas para cada tipo de conta (ex: cheque especial na conta corrente).
    * Aplicação de rendimentos para la Conta Poupança e taxas para a Conta Corrente.

* **Gestão de Produtos Financeiros**:
    * Contratação de produtos para clientes, como **Cartão de Crédito**, **Empréstimos**, **Seguros** e **Investimentos**.
    * Edição de atributos importantes dos produtos (ex: alterar limite do cartão, taxa de juros do empréstimo, etc.).
    * Exclusão de produtos vinculados a um cliente.

* **Persistência de Dados**:
    * Todas as informações são salvas em arquivos binários (`.dat`), garantindo que os dados não sejam perdidos entre as sessões.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem**: Java
* **Interface Gráfica (GUI)**: JavaFX
* **IDE**: O projeto está configurado para o Apache NetBeans
* **Build System**: Ant

---

## 🚀 Como Executar o Projeto

Para executar este projeto, você precisará ter o ambiente de desenvolvimento Java configurado.

### Pré-requisitos

* **JDK 24** ou superior.
* **Apache NetBeans IDE** (versão compatível com JDK 24).

### Passos para Execução

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-seu-repositorio>
    ```

2.  **Abra o projeto no NetBeans:**
    * Abra o NetBeans IDE.
    * Vá em `Arquivo > Abrir Projeto...`.
    * Navegue até a pasta onde você clonou o repositório e selecione-a. O NetBeans reconhecerá a estrutura do projeto.

3.  **Execute a aplicação:**
    * Com o projeto aberto, localize o arquivo `src/bigbank/BigBank.java`.
    * Clique com o botão direito sobre o arquivo e selecione `Executar Arquivo`.
    * A tela de login da aplicação será iniciada.

---
