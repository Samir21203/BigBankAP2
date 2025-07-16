
package model.produtos;

import model.Cliente;
import util.TipoProduto;


public class Investimento implements ProdutoBancario {
    
    private static final long serialVersionUID = 1L;
    
    private long codigo;
    private double valorInicial;
    private double rendimentoMensal;
    private Cliente cliente;
    
    public Investimento(Cliente cliente, double valorInicial, double rendimentoMensal){
        this.cliente = cliente;
        this.valorInicial = valorInicial;
        this.rendimentoMensal = rendimentoMensal;
    }

    public void setRendimentoMensal(double rendimentoMensal) {
        this.rendimentoMensal = rendimentoMensal;
    }
    
    public double calcularValorAposMeses(int meses) {
        return valorInicial * Math.pow(1 + rendimentoMensal, meses);
    }

    // Getters
    public double getValorInicial() {
        return valorInicial;
    }
    
    public double getRendimentoMensal () {
        return rendimentoMensal;
    }
    
    // --- Métodos da Interface ---
    @Override
    public long getCodigoUnico () {
        return this.codigo;
    }
    
    @Override
    public void setCodigoUnico (long codigo) {
        this.codigo = codigo;
    }
    
    @Override
    public Cliente getCliente () {
        return this.cliente;
    }
    
    @Override
    public String getDescricao () {
        return String.format("Investimento: R$ %.2f aplicados", this.valorInicial);
    }
    
    @Override
    public TipoProduto getTipoProduto () {
        return TipoProduto.INVESTIMENTO;
    }
}
