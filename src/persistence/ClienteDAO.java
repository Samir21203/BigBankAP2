
package persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import util.Constantes;


public class ClienteDAO {
    private static final ClienteDAO instancia = new ClienteDAO();
    
    private List<Cliente> clientes;
    
    // Construtor privado que carrega os clientes do arquivo no momento da criação
    private ClienteDAO() {
        this.clientes = carregarClientes();
    }
    
    // Método público para obter a instância única da classe.
    public static ClienteDAO getInstance() {
        return instancia;
    }
    
    private long gerarProximoId() {
        long maxId = 0;
        for (Cliente cliente : clientes) {
            if (cliente.getId() > maxId) {
                maxId = cliente.getId();
            }
        }
        return maxId + 1;
    }
    
    // Salva a lista atual de clientes no arquivo binário.
    private void salvarNoArquivo() {
        try {
            ArquivoUtil.gravar(this.clientes, Constantes.ARQUIVO_CLIENTES);
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }
    
    private List<Cliente> carregarClientes(){
        File arquivo = new File(Constantes.ARQUIVO_CLIENTES);
        if (!arquivo.exists()) {
            return new ArrayList<>(); // Retorna lista vazia se o arquivo não existe
        }
        
        try {
            return (List<Cliente>) ArquivoUtil.ler(Constantes.ARQUIVO_CLIENTES);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
            // Se houver erro de leitura, começa com uma lista vazia para não travar a aplicação
            return new ArrayList<>();
        }
    }
    
    // CRUD: Create, Read, Update, Delete
    
    /**
     * Retorna todos os clientes cadastrados.
     * @return Uma lista com todos os clientes.
     */
    public List<Cliente> getAll() {
        return this.clientes;
    }
    
    /**
     * Adiciona um novo cliente à lista e salva no arquivo.
     * @param cliente a ser adicionado
     */
    public void add(Cliente cliente) {
        cliente.setId(gerarProximoId());
        this.clientes.add(cliente);
        salvarNoArquivo();
    }
    
    /**
     * Atualiza um cliente existente
     * @param clienteAtualizado com informações atualizadas
     */
    public void update(Cliente clienteAtualizado) {
        // Encontra o cliente pelo ID e o substitui na lista
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == clienteAtualizado.getId()) {
                clientes.set(i, clienteAtualizado);
                salvarNoArquivo();
                break;
            }
        }
    }
    
    /**
     * Remove um cliente da lista e salva a alteração no arquivo
     * @param clienteParaExcluir
     */
    public void delete(Cliente clienteParaExcluir) {
        this.clientes.remove(clienteParaExcluir);
        salvarNoArquivo();
    }
    
    /**
     * Busca um cliente pelo CPF
     * @param cpf do cliente a ser buscado
     * @return O objeto Cliente, ou null
     */
    public Cliente getByCpf(String cpf) {
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null;
    }
}
