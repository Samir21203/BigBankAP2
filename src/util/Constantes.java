/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 * Armazena valores constantes usados em toda a aplicação.
 * @author Victor
 */
public class Constantes {
    // Títulos das Janelas (Stages)
    public static final String TITULO_TELA_LOGIN = "BankAP2 - Autenticação";
    public static final String TITULO_TELA_PRINCIPAL = "BankAP2 - Painel Principal";
    public static final String TITULO_GESTAO_CLIENTES = "Gestão de Clientes";
    public static final String TITULO_GESTAO_CONTAS = "Gestão de Contas";
    
    // Nomes dos Arquivos de Persistência
    public static final String ARQUIVO_CLIENTES = "clientes.dat";
    public static final String ARQUIVO_CONTAS = "contas.dat";
    public static final String ARQUIVO_GESTORES = "gestores.dat";
    public static final String ARQUIVO_PRODUTOS = "produtos.dat";
    
    // Mensagens de Alerta e Confirmação
    public static final String ERRO_LOGIN_INVALIDO = "Login ou senha inválidos. Tente novamente.";
    public static final String ERRO_CAMPO_OBRIGATORIO = "Erro de Validação";
    public static final String CONFIRMACAO_EXCLUSAO_TITULO = "Confirmar Exclusão";
    public static final String CONFIRMACAO_EXCLUSAO_MENSAGEM = "Você tem certeza que deseja excluir este item? Esta ação não pode ser desfeita.";
    
    private Constantes() {
    }
}
