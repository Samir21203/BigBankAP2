
package model.produtos;

import model.Cliente;
import util.TipoProduto;

public class Emprestimo implements ProdutoBancario {
    
    private static final long serialVersionUID = 1L;
    
    private long codigo;
    private double valor;
    private double juros;
    private int parcelas;
    private Cliente cliente;
    
    public Emprestimo(double valor, double juros, int parcelas, Cliente cliente){
        this.valor = valor;
        this.juros = juros;
        this.parcelas = parcelas;
        this.cliente = cliente;
    }
    
    public double calcularValorParcela() {
        // Tabela Price
        double taxa = this.juros;
        double valorPresente = this.valor;
        return (valorPresente * taxa) / 1 - Math.pow(1 + taxa, -this.parcelas);
    }

    public double calcularValorFinal() {
        return valor * Math.pow(1 + juros, parcelas);
    }

    //getters
    public double getValor(){
        return valor;
    }
    public double getJuros(){
        return juros;
    }
    public int getParcelas(){
        return parcelas;
    }
    
    //setters
    public void setJuros(double juros){
        this.juros = juros;
    }
    public void setParcelas(int parcelas){
        this.parcelas = parcelas;
    }
    
    // --- Implementação dos métodos da interface ---
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
        return String.format("Empréstimo: %.2f em %d parcelas", this.valor, this.parcelas);
    }
    
    @Override
    public TipoProduto getTipoProduto() {
        return TipoProduto.EMPRESTIMO;
    }
}
