
package model.produtos;

//import model.produtos.ProdutoBancario;
import model.Cliente;
import util.TipoProduto;

public class CartaoCredito implements ProdutoBancario {
    
    private static final long serialVersionUID = 1L;
    
    private long codigo;
    private final String validade;
    private double limite;
    private double fatura;
    private final Cliente cliente;
    
    public CartaoCredito (String validade, double limite, Cliente cliente){
        this.cliente = cliente;
        this.limite = limite;
        this.validade = validade;
        this.fatura = 0;
    }
    
    public boolean realizarCompra(double valor){
        boolean compra = false;
        if(valor <= (this.limite - this.fatura)) {
        limite -= valor;
        fatura += valor;
        compra = true;
        }
        return compra;
    }
    
    public void pagarFatura(double valor){
        fatura -= valor;
    }
    
    //getters    
    public String getValidade(){
        return validade;
    }
    public double getLimite(){
        return limite;
    }
    public double getFatura(){
        return fatura;
    }
    
    //setters
    public void setLimite(double limite){
        this.limite = limite;
    }
    
    // --- Métodos da interface ---
        @Override
    public long getCodigoUnico(){
        return codigo;
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
    public String getDescricao() {
        return String.format("Cartão de Crédito - Limite de: R$ %.2f", this.limite);
    }
    
    @Override 
    public TipoProduto getTipoProduto () {
        return TipoProduto.CARTAO_CREDITO;
    }
}
