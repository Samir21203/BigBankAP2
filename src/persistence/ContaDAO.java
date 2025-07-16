
package persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.contas.Conta;
import util.Constantes;


public class ContaDAO {
    
    private static final ContaDAO instancia = new ContaDAO();
    private List<Conta> contas;
    
    // Construtor privado que carrega os clientes do arquivo no momento da criação
    private ContaDAO() {
        this.contas = carregarContas();
    }
    
    // Método público para obter a instância única da classe.
    public static ContaDAO getInstance() {
        return instancia;
    }
    
    /**
     * Gera o próximo número de conta disponível
     * @return long com o próximo número de conta
     */
    private long gerarProximoNumeroConta() {
        long maxNumero = 0L;
        
        for (Conta conta : contas) {
            if (conta.getNumeroConta() > maxNumero) {
                maxNumero = conta.getNumeroConta();
            }
        }
        return maxNumero + 1;
    }
    
    // Salva a lista atual de clientes no arquivo binário.
    private void salvarNoArquivo() {
        try {
            ArquivoUtil.gravar(this.contas, Constantes.ARQUIVO_CONTAS);
        } catch (IOException e) {
            System.err.println("Erro ao salvar contas: " + e.getMessage());
        }
    }
    
    private List<Conta> carregarContas(){
        File arquivo = new File(Constantes.ARQUIVO_CONTAS);
        if (!arquivo.exists()) {
            return new ArrayList<>(); // Retorna lista vazia se o arquivo não existe
        }
        
        try {
            return (List<Conta>) ArquivoUtil.ler(Constantes.ARQUIVO_CONTAS);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar contas: " + e.getMessage());
            // Se houver erro de leitura, começa com uma lista vazia para não travar a aplicação
            return new ArrayList<>();
        }
    }
    
    // CRUD: Create, Read, Update, Delete
    
    /**
     * Retorna todos os clientes cadastrados.
     * @return Uma lista com todos os clientes.
     */
    public List<Conta> getAll() {
        return this.contas;
    }
    
    /**
     * Adiciona uma nova Conta à lista e salva no arquivo.
     * @param conta a ser adicionada
     */
    public void add(Conta conta) {
        conta.setNumeroConta(gerarProximoNumeroConta());
        this.contas.add(conta);
        salvarNoArquivo();
    }
    
    /**
     * Atualiza uma conta existente
     * @param contaAtualizada com informações atualizadas
     */
    public void update(Conta contaAtualizada) {
        // Encontra a Conta pelo número e o substitui na lista
        for (int i = 0; i < contas.size(); i++) {
            if (contas.get(i).getNumeroConta() == contaAtualizada.getNumeroConta()) {
                contas.set(i, contaAtualizada);
                salvarNoArquivo();
                break;
            }
        }
    }
    
    /**
     * Remove uma conta da lista e salva a alteração no arquivo
     * @param contaParaExcluir
     */
    public void delete(Conta contaParaExcluir) {
        this.contas.remove(contaParaExcluir);
        salvarNoArquivo();
    }
    
    /**
     * Busca uma conta pelo número de conta
     * @param numeroConta da conta a ser buscada
     * @return O objeto Conta, ou null
     */
    public Conta getByNumeroConta (long numeroConta) {
        for (Conta conta : contas) {
            if (conta.getNumeroConta() == numeroConta) {
                return conta;
            }
        }
        return null;
    }
}
