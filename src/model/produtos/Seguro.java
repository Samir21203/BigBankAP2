
package model.produtos;

import model.Cliente;
import util.TipoProduto;

public class Seguro implements ProdutoBancario {
    
    private static final long serialVersionUID = 1L;
    
    private long codigo;
    private String tipoSeguro; // Vida, Automóvel, etc.
    private double valorMensal;
    private Cliente cliente;

    public Seguro(Cliente cliente, String tipo, double valorMensal){
        this.cliente = cliente;
        this.tipoSeguro = tipo;
        this.valorMensal = valorMensal;
    }

    //getters
    public String getTipo(){
        return tipoSeguro;
    }
    public double getValorMensal(){
        return valorMensal;
    }
    
    public void setValorMensal(double valorMensal){
        this.valorMensal = valorMensal;
    }
    
    // --- Métodos das Interfaces ---
    @Override
    public long getCodigoUnico () {
        return this.codigo;
    }
    
    @Override
    public void setCodigoUnico (long codigo) {
        this.codigo = codigo;
    }
    
    @Override
    public Cliente getCliente(){
        return cliente;
    }
    
    @Override 
    public String getDescricao () {
        return String.format("Seguro %s: R$ %.2f/mês", this.tipoSeguro, this.valorMensal);
    }
    
    @Override
    public TipoProduto getTipoProduto () {
        return TipoProduto.SEGURO;
    }
}

