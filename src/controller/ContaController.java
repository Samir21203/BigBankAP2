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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Cliente;
import model.contas.Conta;
import model.contas.ContaCorrente;
import model.contas.ContaPoupanca;
import persistence.ClienteDAO;
import persistence.ContaDAO;
import util.Formatador;
import util.TipoConta;

public class ContaController implements Initializable {

    // Componentes FXML (adapte os fx:id no seu conta.fxml para estes nomes)
    @FXML private TableView<Conta> tableViewContas;
    @FXML private TableColumn<Conta, Long> columnNumero;
    @FXML private TableColumn<Conta, Cliente> columnCliente;
    @FXML private TableColumn<Conta, TipoConta> columnTipo;

    // Seção para criar nova conta
    @FXML private ComboBox<Cliente> comboBoxCliente;
    @FXML private ComboBox<TipoConta> comboBoxTipoConta;
    @FXML private Button buttonNewConta;

    // Seção de detalhes e ações
    @FXML private Label labelNumeroConta;
    @FXML private Label labelSaldo;
    @FXML private Label labelClienteNome;
    @FXML private Button buttonDeleteConta;

    private Conta contaSelecionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // --- Configuração da Tabela ---
        columnNumero.setCellValueFactory(new PropertyValueFactory<>("numeroConta"));
        columnCliente.setCellValueFactory(new PropertyValueFactory<>("cliente")); // Usa o toString() do Cliente
        columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        // Adiciona um "ouvinte" para quando uma linha da tabela for selecionada
        tableViewContas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> preencherDetalhes(newValue));

        // --- Carregamento de Dados Iniciais ---
        carregarComboBoxes();
        carregarTabelaContas();
        limparDetalhes();
    }

    @FXML
    private void handleNewConta() {
        Cliente cliente = comboBoxCliente.getSelectionModel().getSelectedItem();
        TipoConta tipo = comboBoxTipoConta.getSelectionModel().getSelectedItem();

        if (cliente == null || tipo == null) {
            mostrarAlerta(AlertType.ERROR, "Erro ao Criar Conta", "Por favor, selecione um cliente e um tipo de conta.");
            return;
        }

        Conta novaConta;
        if (tipo == TipoConta.CONTA_CORRENTE) {
            novaConta = new ContaCorrente(cliente, 1000.0, 15.0); // Limite e taxa padrão
        } else {
            novaConta = new ContaPoupanca(cliente, 0.005, 10); // Rendimento e dia padrão
        }
        
        ContaDAO.getInstance().add(novaConta);
        
        carregarTabelaContas(); // Atualiza a tabela na tela
        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Nova " + tipo.getDescricao() + " criada para " + cliente.getNome());
    }
    
    @FXML
    private void handleDeleteConta() {
        if (contaSelecionada == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhuma Conta Selecionada", "Por favor, selecione uma conta na tabela para excluir.");
            return;
        }

        Optional<ButtonType> resultado = mostrarAlertaConfirmacao("Confirmar Exclusão",
                "Tem certeza que deseja excluir a conta " + contaSelecionada.getNumeroConta() + "?");

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            ContaDAO.getInstance().delete(contaSelecionada);
            carregarTabelaContas();
            limparDetalhes();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Conta excluída com sucesso!");
        }
    }

    private void carregarTabelaContas() {
        ObservableList<Conta> obsContas = FXCollections.observableArrayList(ContaDAO.getInstance().getAll());
        tableViewContas.setItems(obsContas);
    }

    private void carregarComboBoxes() {
        List<Cliente> clientes = ClienteDAO.getInstance().getAll();
        comboBoxCliente.setItems(FXCollections.observableArrayList(clientes));
        
        comboBoxTipoConta.setItems(FXCollections.observableArrayList(TipoConta.values()));
    }
    
    private void preencherDetalhes(Conta conta) {
        this.contaSelecionada = conta;
        if (conta != null) {
            labelNumeroConta.setText(String.valueOf(conta.getNumeroConta()));
            labelSaldo.setText(Formatador.formatarMoeda(conta.getSaldo()));
            labelClienteNome.setText(conta.getCliente().getNome());
        } else {
            limparDetalhes();
        }
    }
    
    private void limparDetalhes() {
        contaSelecionada = null;
        labelNumeroConta.setText("-");
        labelSaldo.setText("-");
        labelClienteNome.setText("-");
        tableViewContas.getSelectionModel().clearSelection();
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