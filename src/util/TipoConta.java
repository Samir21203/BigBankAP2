/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Victor
 */
public enum TipoConta {
    CONTA_CORRENTE("Conta Corrente"), 
    CONTA_POUPANCA("Conta Poupança");
    
    private final String descricao;
    
    TipoConta(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public String toString() {
        return this.getDescricao();
    }
}
