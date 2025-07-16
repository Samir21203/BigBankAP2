
package model.contas;

import java.io.Serializable;
import java.time.LocalDate;
import model.Cliente;
import util.TipoConta;

public class ContaPoupanca extends Conta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private double taxaRendimento;
    private int diaRendimento; // dia simulado
    private LocalDate dataUltimoRendimento; // contador de ciclos simulando meses

    public ContaPoupanca(Cliente cliente, double taxaRendimento, int diaRendimento) {
        super(cliente);
        this.taxaRendimento = taxaRendimento;
        this.diaRendimento = (diaRendimento < 1 || diaRendimento > 28) ? 1 : diaRendimento;
        this.dataUltimoRendimento = null; // começa com um mês disponível
    }

    public double getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }

    public int getDiaRendimento() {
        return diaRendimento;
    }

    public void setDiaRendimento(int diaRendimento) {
        this.diaRendimento = diaRendimento;
    }

    public boolean aplicarRendimento() {
        LocalDate hoje = LocalDate.now();
        
        // Condição 1: O rendimento deste mês ja foi aplicado?
        // Verifica se o último rendimento foi no mesmo mês e ano de hoje.
        if (dataUltimoRendimento != null &&
                dataUltimoRendimento.getMonth() == hoje.getMonth() &&
                dataUltimoRendimento.getYear() == hoje.getYear()) {
            return false; // Já rendeu neste mês
        }
        
        // Condição 2: Hoje é o dia do aniversário da conta?
        if (hoje.getDayOfMonth() == this.diaRendimento) {
            if (getSaldo() > 0) {
                double rendimento = getSaldo() * this.taxaRendimento;
                
                super.depositar(rendimento);
                
                if (!historico.isEmpty()) {
                    historico.set(historico.size() - 1, String.format("Rendimento: + R$ %.2f", rendimento));
                }
                
                // Atualiza a data do último rendimento para hoje
                this.dataUltimoRendimento = hoje;
                return true;
            }
        }
        return false;
    }

    @Override
    public TipoConta getTipo() {
        return TipoConta.CONTA_POUPANCA;
    }
    
}
