package controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Cliente;
import model.produtos.*;
import persistence.ClienteDAO;
import persistence.ProdutoDAO;
import util.Formatador;
import util.TipoProduto;

public class ProdutosController implements Initializable {

    // Componentes FXML
    @FXML private TableView<ProdutoBancario> tableViewProdutos;
    @FXML private TableColumn<ProdutoBancario, Long> columnId;
    @FXML private TableColumn<ProdutoBancario, TipoProduto> columnTipo;
    @FXML private TableColumn<ProdutoBancario, String> columnDescricao;
    @FXML private TableColumn<ProdutoBancario, Cliente> columnCliente;
    @FXML private ComboBox<Cliente> comboBoxCliente;
    @FXML private ComboBox<TipoProduto> comboBoxTipoProduto;
    @FXML private Button buttonVoltar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabela();
        carregarComboBoxes();
        carregarTabelaProdutos();
    }

    private void configurarTabela() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("codigoUnico"));
        columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipoProduto"));
        columnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        columnCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
    }

    private void carregarComboBoxes() {
        List<Cliente> clientes = ClienteDAO.getInstance().getAll();
        comboBoxCliente.setItems(FXCollections.observableArrayList(clientes));

        ObservableList<TipoProduto> tiposContrataveis = FXCollections.observableArrayList(TipoProduto.values());
        tiposContrataveis.remove(TipoProduto.TRANSACAO);
        comboBoxTipoProduto.setItems(tiposContrataveis);
    }

    private void carregarTabelaProdutos() {
        ObservableList<ProdutoBancario> obsProdutos = FXCollections.observableArrayList(ProdutoDAO.getInstance().getAll());
        tableViewProdutos.setItems(obsProdutos);
    }


    @FXML
    private void handleContratarProduto() {
        Cliente cliente = comboBoxCliente.getSelectionModel().getSelectedItem();
        TipoProduto tipo = comboBoxTipoProduto.getSelectionModel().getSelectedItem();

        if (cliente == null || tipo == null) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Selecione um cliente e um tipo de produto.");
            return;
        }

        ProdutoBancario novoProduto = null;
        try {
            switch (tipo) {
                case CARTAO_CREDITO:
                    double limite = Double.parseDouble(mostrarDialogoEntrada("Limite do Cartão", "Digite o limite de crédito:"));
                    novoProduto = new CartaoCredito("12/29", limite, cliente);
                    break;
                case EMPRESTIMO:
                    double valor = Double.parseDouble(mostrarDialogoEntrada("Valor do Empréstimo", "Digite o valor a ser emprestado:"));
                    int parcelas = Integer.parseInt(mostrarDialogoEntrada("Número de Parcelas", "Digite a quantidade de parcelas:"));
                    novoProduto = new Emprestimo(valor, 0.05, parcelas, cliente);
                    break;
                case SEGURO:
                    String tipoSeguro = mostrarDialogoEntrada("Tipo de Seguro", "Digite o tipo (Ex: Vida, Automóvel):");
                    double valorMensal = Double.parseDouble(mostrarDialogoEntrada("Valor Mensal", "Digite o valor da mensalidade:"));
                    novoProduto = new Seguro(cliente, tipoSeguro, valorMensal);
                    break;
                case INVESTIMENTO:
                     double valorInicial = Double.parseDouble(mostrarDialogoEntrada("Valor do Investimento", "Digite o valor inicial:"));
                    novoProduto = new Investimento(cliente, valorInicial, 0.01);
                    break;
                default:
                    return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Erro de Entrada", "Valor inválido inserido.");
        } catch (Exception e) {
        }

        if (novoProduto != null) {
            ProdutoDAO.getInstance().add(novoProduto);
            carregarTabelaProdutos();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", tipo.getNomeProduto() + " contratado para " + cliente.getNome());
        }
    }

    @FXML
    private void handleEditarProduto() {
        ProdutoBancario produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) {
            mostrarAlerta(AlertType.WARNING, "Seleção Vazia", "Selecione um produto para editar.");
            return;
        }

        boolean alterado = false;
        try {
            if (produto instanceof CartaoCredito) {
                CartaoCredito cartao = (CartaoCredito) produto;
                double novoLimite = Double.parseDouble(mostrarDialogoEntrada("Editar Limite do Cartão", "Limite atual: " + Formatador.formatarMoeda(cartao.getLimite()) + "\n\nDigite o novo limite:"));
                cartao.setLimite(novoLimite);
                alterado = true;
            } else if (produto instanceof Emprestimo) {
                Emprestimo emprestimo = (Emprestimo) produto;
                double novoJuros = Double.parseDouble(mostrarDialogoEntrada("Editar Juros do Empréstimo", "Juros atual: " + emprestimo.getJuros() * 100 + "%\n\nDigite a nova taxa (ex: 0.05 para 5%):"));
                emprestimo.setJuros(novoJuros);
                alterado = true;
            } else if (produto instanceof Seguro) {
                Seguro seguro = (Seguro) produto;
                double novoValorMensal = Double.parseDouble(mostrarDialogoEntrada("Editar Valor do Seguro", "Valor mensal atual: " + Formatador.formatarMoeda(seguro.getValorMensal()) + "\n\nDigite o novo valor mensal:"));
                seguro.setValorMensal(novoValorMensal);
                alterado = true;
            } else if (produto instanceof Investimento) {
                Investimento investimento = (Investimento) produto;
                double novoRendimento = Double.parseDouble(mostrarDialogoEntrada("Editar Rendimento", "Rendimento mensal atual: " + investimento.getRendimentoMensal() * 100 + "%\n\nDigite a nova taxa (ex: 0.01 para 1%):"));
                investimento.setRendimentoMensal(novoRendimento);
                alterado = true;
            } else {
                mostrarAlerta(AlertType.INFORMATION, "Não Editável", "Este tipo de produto não pode ser editado.");
            }

            if (alterado) {
                ProdutoDAO.getInstance().update(produto);
                tableViewProdutos.refresh(); 
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Produto atualizado com sucesso.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Erro de Entrada", "Valor inválido inserido.");
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void handleDeleteProduto() {
        ProdutoBancario produtoSelecionado = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum Produto Selecionado", "Selecione um produto para remover.");
            return;
        }
        Optional<ButtonType> resultado = mostrarAlertaConfirmacao("Confirmar Remoção", "Tem certeza que deseja remover o produto: " + produtoSelecionado.getDescricao() + "?");
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            ProdutoDAO.getInstance().delete(produtoSelecionado);
            carregarTabelaProdutos();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Produto removido com sucesso!");
        }
    }

    private String mostrarDialogoEntrada(String titulo, String cabecalho) throws Exception {
        TextInputDialog dialog = new TextInputDialog();
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