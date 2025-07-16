
package model.contas;

import java.io.Serializable;
import model.Cliente;
import util.TipoConta;


public class ContaCorrente extends Conta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final double TAXA_SAQUE = 0.002;
    
    private double limiteChequeEspecial;
    private double taxaManutencaoMensal;

    public ContaCorrente(Cliente cliente, double limiteChequeEspecial, double taxaManutencaoMensal) {
        super(cliente);
        this.limiteChequeEspecial = limiteChequeEspecial;
        this.taxaManutencaoMensal = taxaManutencaoMensal;
    }

    // --- Getters e Setters ---
    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(double limiteChequeEspecial) {
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    public double getTaxaManutencaoMensal() {
        return taxaManutencaoMensal;
    }

    public void setTaxaManutencaoMensal(double taxaManutencaoMensal) {
        this.taxaManutencaoMensal = taxaManutencaoMensal;
    }
    
    public void aplicarTaxaManutencao() {
        setSaldo(getSaldo() - this.taxaManutencaoMensal);
        historico.add(String.format("Taxa de Manutenção: - R$ %.2f", this.taxaManutencaoMensal));
    }

    @Override
    public boolean sacar(double valor) {
        if (valor > 0) {
            double taxa = valor * TAXA_SAQUE;
            double total = valor + taxa;

            if (total <= (getSaldo() + limiteChequeEspecial)) {
                setSaldo(getSaldo() - total);
                historico.add(String.format("Saque: - R$ %.2f (Taxa: R$ %.2f)", valor, taxa));
                return true;
            }
        }
        return false;
    }

    @Override
    public TipoConta getTipo() {
        return TipoConta.CONTA_CORRENTE;
    }
    
}
