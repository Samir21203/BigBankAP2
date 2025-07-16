
package model.contas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import util.TipoConta;

public abstract class Conta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final double TAXA_SAQUE = 0.002;
    
    private long numeroConta;
    private Cliente cliente;
    private double saldo;
    protected List <String> historico; //utilizei protected para que as classes filhas possam utilizar o historico.add
    
        
    public Conta(Cliente cliente){
        this.cliente=cliente;
        saldo = 0.0;
        historico = new ArrayList();
    }
        
    // --- Getters e Setters
    public long getNumeroConta() {
        return numeroConta;
    }
    
    // Número definido pelo DAO
    public void setNumeroConta(long numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public double getSaldo() {
        return saldo;
    }
    
    protected void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    // --- Métodos  de Negócio ---
    public boolean depositar(double valor) {
        if(valor > 0) {
            this.saldo+= valor;
            this.historico.add(String.format("Depósito: + R$ %.2f", valor));
            return true;
        }
        return false;
    }
    
    public boolean sacar(double valor) {
        if(valor > 0) {
            double taxa = valor * TAXA_SAQUE;
            double total = valor + taxa;
                        
            if(total <= saldo) {
                saldo -= total;
                historico.add(String.format("Saque: - R$ %.2f (Taxa: R$ %.2f)", valor, taxa));
                return true;
            }
        }
        return false;
    }    
        
    //public String recuperarDadosConta() {
    //    return "-----------------------" + 
    //           "\nNúmero da conta: " + getNumeroConta() +
    //           "\nSaldo: " + getSaldo() + 
    //           "\nTipo: " + this.getTipo() + 
    //           "\n-----------------------\n"; 
    //}
    
    public List<String> getHistorico(){
        return historico;
    }
    
    //getTipo
    public abstract TipoConta getTipo();
    
}
