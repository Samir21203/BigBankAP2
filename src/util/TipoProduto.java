/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Victor
 */
public enum TipoProduto {
    
    CARTAO_CREDITO("Cartão de Crédito"),
    EMPRESTIMO("Empréstimo Pessoal"),
    INVESTIMENTO("Investimento"),
    SEGURO("Seguro"),
    TRANSACAO("Transação");
    
    private final String nomeProduto;
    
    TipoProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
    
    public String getNomeProduto() {
        return nomeProduto;
    }
    
    @Override
    public String toString() {
        return this.getNomeProduto();
    }
}
