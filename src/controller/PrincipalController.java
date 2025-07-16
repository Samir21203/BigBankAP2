package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cliente;
import model.contas.Conta;
import model.contas.ContaCorrente;
import model.contas.ContaPoupanca;
import model.produtos.CartaoCredito;
import model.produtos.Emprestimo;
import model.produtos.Investimento;
import model.produtos.ProdutoBancario;
import model.produtos.Seguro;
import persistence.ClienteDAO;
import persistence.ContaDAO;
import persistence.ProdutoDAO;
import util.TipoConta;
import util.TipoProduto;

public class PrincipalController implements Initializable {

    @FXML private AnchorPane paneClientes;
    @FXML private AnchorPane paneContas;
    @FXML private AnchorPane paneProdutos;

    @FXML private TableView<Cliente> tableClientes;
    @FXML private TableColumn<Cliente, Long> colClienteId;
    @FXML private TableColumn<Cliente, String> colClienteCpf;
    @FXML private TableColumn<Cliente, String> colClienteNome;

    @FXML private TableView<Conta> tableContas;
    @FXML private TableColumn<Conta, Long> colContaNumero;
    @FXML private TableColumn<Conta, TipoConta> colContaTipo;
    @FXML private TableColumn<Conta, Cliente> colContaCliente;
    
    @FXML private TableView<ProdutoBancario> tableProdutos;
    @FXML private TableColumn<ProdutoBancario, Long> colProdutoId;
    @FXML private TableColumn<ProdutoBancario, TipoProduto> colProdutoTipo;
    @FXML private TableColumn<ProdutoBancario, String> colProdutoDescricao;
    @FXML private TableColumn<ProdutoBancario, Cliente> colProdutoCliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColunas();
        handleMostrarClientes(); 
    }

    private void configurarColunas() {
        colClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClienteCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        colContaNumero.setCellValueFactory(new PropertyValueFactory<>("numeroConta"));
        colContaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colContaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        colProdutoId.setCellValueFactory(new PropertyValueFactory<>("codigoUnico"));
        colProdutoTipo.setCellValueFactory(new PropertyValueFactory<>("tipoProduto"));
        colProdutoDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colProdutoCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
    }

    @FXML
    private void handleMostrarClientes() {
        alternarPainelVisivel(paneClientes);
        tableClientes.setItems(FXCollections.observableArrayList(ClienteDAO.getInstance().getAll()));
    }

    @FXML
    private void handleMostrarContas() {
        alternarPainelVisivel(paneContas);
        tableContas.setItems(FXCollections.observableArrayList(ContaDAO.getInstance().getAll()));
    }

    @FXML
    private void handleMostrarProdutos() {
        alternarPainelVisivel(paneProdutos);
        tableProdutos.setItems(FXCollections.observableArrayList(ProdutoDAO.getInstance().getAll()));
    }
    
    @FXML
    private void handleAdicionarCliente() {
        abrirFormularioCliente(null);
    }
    @FXML
    private void handleEditarCliente() {
        Cliente cliente = tableClientes.getSelectionModel().getSelectedItem();
        if (cliente == null) {
            mostrarAlerta(AlertType.WARNING, "Seleção Vazia", "Selecione um cliente para editar.");
            return;
        }
        abrirFormularioCliente(cliente);
    }
    @FXML
    private void handleExcluirCliente() {

        Cliente clienteParaExcluir = tableClientes.getSelectionModel().getSelectedItem();

        if (clienteParaExcluir == null) {
            mostrarAlerta(AlertType.WARNING, "Seleção Vazia", "Selecione um cliente na tabela para excluir.");
            return;
        }
        
        Optional<ButtonType> resultado = mostrarAlertaConfirmacao("Confirmar Exclusão em Cascata",
                "Tem certeza que deseja excluir o cliente " + clienteParaExcluir.getNome() + "?\n\nATENÇÃO: Todas as contas e produtos associados a este cliente também serão excluídos permanentemente.");

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            long idCliente = clienteParaExcluir.getId();

            List<Conta> todasAsContas = ContaDAO.getInstance().getAll();
            for (Conta conta : new ArrayList<>(todasAsContas)) {
                if (conta.getCliente().getId() == idCliente) {
                    ContaDAO.getInstance().delete(conta);
                }
            }

            List<ProdutoBancario> todosOsProdutos = ProdutoDAO.getInstance().getAll();
            for (ProdutoBancario produto : new ArrayList<>(todosOsProdutos)) {
                if (produto.getCliente().getId() == idCliente) {
                    ProdutoDAO.getInstance().delete(produto);
                }
            }

            ClienteDAO.getInstance().delete(clienteParaExcluir);

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Cliente e todos os seus dados foram excluídos.");
            handleMostrarClientes();
        }
    }

    @FXML
    private void handleCriarConta() {
        try {
            Cliente cliente = selecionarClienteDialogo("Criar Nova Conta", "Selecione o titular da conta:");
            if (cliente == null) return;

            ChoiceDialog<TipoConta> dialogoTipo = new ChoiceDialog<>(TipoConta.CONTA_CORRENTE, TipoConta.values());
            dialogoTipo.setTitle("Tipo de Conta");
            dialogoTipo.setHeaderText("Selecione o tipo da nova conta para " + cliente.getNome());
            dialogoTipo.setContentText("Tipo:");
            Optional<TipoConta> tipoResultado = dialogoTipo.showAndWait();

            if (tipoResultado.isPresent()) {
                TipoConta tipo = tipoResultado.get();
                Conta novaConta;
                
                if (tipo == TipoConta.CONTA_CORRENTE) {
                    novaConta = new ContaCorrente(cliente, 1000.0, 15.0);
                } else {
                    novaConta = new ContaPoupanca(cliente, 0.005, 10);
                } 
                
                ContaDAO.getInstance().add(novaConta);
                
                handleMostrarContas();
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Conta criada para " + cliente.getNome());
            }
        } catch (Exception e) {
            mostrarAlerta(AlertType.WARNING, "Operação Cancelada", e.getMessage());
        }
    }
    @FXML
    private void handleExcluirConta() {
        Conta conta = tableContas.getSelectionModel().getSelectedItem();
        if (conta == null) {
            mostrarAlerta(AlertType.WARNING, "Seleção Vazia", "Selecione uma conta para excluir.");
            return;
        }
        Optional<ButtonType> res = mostrarAlertaConfirmacao("Excluir Conta", "Deseja excluir a conta " + conta.getNumeroConta() + "?");
        if (res.isPresent() && res.get() == ButtonType.OK) {
            ContaDAO.getInstance().delete(conta);
            handleMostrarContas();
        }
    }

    @FXML
    private void handleContratarProduto() {
        try {
            Cliente cliente = selecionarClienteDialogo("Contratar Produto", "Selecione o cliente:");
            if (cliente == null) return;
            
            ObservableList<TipoProduto> tipos = FXCollections.observableArrayList(TipoProduto.values());
            tipos.remove(TipoProduto.TRANSACAO);
            
            ChoiceDialog<TipoProduto> dialogoTipo = new ChoiceDialog<>(tipos.get(0), tipos);
            dialogoTipo.setTitle("Tipo de Produto");
            dialogoTipo.setHeaderText("Selecione o produto para " + cliente.getNome());
            dialogoTipo.setContentText("Produto:");
            Optional<TipoProduto> tipoResultado = dialogoTipo.showAndWait();

            if (tipoResultado.isPresent()) {
                ProdutoBancario novoProduto = criarProdutoComDados(tipoResultado.get(), cliente);
                if (novoProduto != null) {
                    ProdutoDAO.getInstance().add(novoProduto);
                    handleMostrarProdutos();
                    mostrarAlerta(AlertType.INFORMATION, "Sucesso", tipoResultado.get().getNomeProduto() + " contratado.");
                }
            }
        } catch (NumberFormatException e){
            mostrarAlerta(AlertType.ERROR, "Erro de Entrada", "Valor numérico inválido inserido.");
        } catch (Exception e) {
        }
    }
    @FXML
    private void handleEditarProduto() {
        ProdutoBancario produto = tableProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) {
            mostrarAlerta(AlertType.WARNING, "Seleção Vazia", "Selecione um produto para editar.");
            return;
        }

        boolean alterado = false;
        try {
            if (produto instanceof CartaoCredito) {
                CartaoCredito c = (CartaoCredito) produto;
                double novoLimite = Double.parseDouble(mostrarDialogoEntrada("Editar Limite", "Novo limite:", String.valueOf(c.getLimite())));
                c.setLimite(novoLimite);
                alterado = true;
            } else if (produto instanceof Emprestimo) {
                Emprestimo e = (Emprestimo) produto;
                double novoJuros = Double.parseDouble(mostrarDialogoEntrada("Editar Juros", "Nova taxa (ex: 0.05):", String.valueOf(e.getJuros())));
                e.setJuros(novoJuros);
                alterado = true;
            } else if (produto instanceof Seguro) {
                Seguro s = (Seguro) produto;
                double novoValor = Double.parseDouble(mostrarDialogoEntrada("Editar Valor Mensal", "Novo valor:", String.valueOf(s.getValorMensal())));
                s.setValorMensal(novoValor);
                alterado = true;
            } else if (produto instanceof Investimento) {
                Investimento i = (Investimento) produto;
                double novoRendimento = Double.parseDouble(mostrarDialogoEntrada("Editar Rendimento", "Novo rendimento (ex: 0.01):", String.valueOf(i.getRendimentoMensal())));
                i.setRendimentoMensal(novoRendimento);
                alterado = true;
            }
            
            if (alterado) {
                ProdutoDAO.getInstance().update(produto);
                tableProdutos.refresh();
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Produto atualizado.");
            } else {
                 mostrarAlerta(AlertType.INFORMATION, "Aviso", "Edição não implementada para este tipo de produto.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Erro de Entrada", "Valor numérico inválido inserido.");
        } catch (Exception e) {
        }
    }
    @FXML
    private void handleExcluirProduto() {
        ProdutoBancario produto = tableProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) {
            mostrarAlerta(AlertType.WARNING, "Seleção Vazia", "Selecione um produto para excluir.");
            return;
        }
        Optional<ButtonType> res = mostrarAlertaConfirmacao("Excluir Produto", "Deseja excluir: " + produto.getDescricao() + "?");
        if (res.isPresent() && res.get() == ButtonType.OK) {
            ProdutoDAO.getInstance().delete(produto);
            handleMostrarProdutos();
        }
    }
    
    private void alternarPainelVisivel(AnchorPane painelVisivel) {
        paneClientes.setVisible(painelVisivel == paneClientes);
        paneContas.setVisible(painelVisivel == paneContas);
        paneProdutos.setVisible(painelVisivel == paneProdutos);
    }

    private void abrirFormularioCliente(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cliente.fxml"));
            Parent root = loader.load();
            ClienteController controllerDestino = loader.getController();
            controllerDestino.initData(cliente);
            Stage stage = new Stage();
            stage.setTitle(cliente == null ? "Adicionar Cliente" : "Editar Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            handleMostrarClientes(); // Atualiza a tabela de clientes
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private ProdutoBancario criarProdutoComDados(TipoProduto tipo, Cliente cliente) throws Exception {
        switch (tipo) {
            case CARTAO_CREDITO:
                double limite = Double.parseDouble(mostrarDialogoEntrada("Limite do Cartão", "Digite o limite de crédito:", "5000.00"));
                return new CartaoCredito("12/29", limite, cliente);
            case EMPRESTIMO:
                double valor = Double.parseDouble(mostrarDialogoEntrada("Valor do Empréstimo", "Digite o valor:", "10000.00"));
                int parcelas = Integer.parseInt(mostrarDialogoEntrada("Nº de Parcelas", "Digite as parcelas:", "24"));
                return new Emprestimo(valor, 0.05, parcelas, cliente);
            case SEGURO:
                String tipoSeguro = mostrarDialogoEntrada("Tipo de Seguro", "Digite o tipo (Ex: Vida):", "Vida Essencial");
                double valorMensal = Double.parseDouble(mostrarDialogoEntrada("Valor Mensal", "Digite a mensalidade:", "99.90"));
                return new Seguro(cliente, tipoSeguro, valorMensal);
            case INVESTIMENTO:
                double valorInicial = Double.parseDouble(mostrarDialogoEntrada("Valor do Investimento", "Digite o valor inicial:", "1000.00"));
                return new Investimento(cliente, valorInicial, 0.01);
            default: 
                mostrarAlerta(AlertType.ERROR, "Erro", "Criação não implementada para este tipo de produto.");
                return null;
        }
    }
    
    private Cliente selecionarClienteDialogo(String titulo, String cabecalho) {
        List<Cliente> clientes = ClienteDAO.getInstance().getAll();
        if (clientes.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Aviso", "Nenhum cliente cadastrado.");
            return null;
        }
        ChoiceDialog<Cliente> dialog = new ChoiceDialog<>(clientes.get(0), clientes);
        dialog.setTitle(titulo);
        dialog.setHeaderText(cabecalho);
        return dialog.showAndWait().orElse(null);
    }
    
    private String mostrarDialogoEntrada(String titulo, String cabecalho, String valorPadrao) throws Exception {
        TextInputDialog dialog = new TextInputDialog(valorPadrao);
        dialog.setTitle(titulo);
        dialog.setHeaderText(cabecalho);
        dialog.setContentText("Valor:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().isBlank()) {
            return result.get();
        }
        throw new Exception("Operação cancelada ou entrada vazia.");
    }
    
    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private Optional<ButtonType> mostrarAlertaConfirmacao(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        return alerta.showAndWait();
    }
}