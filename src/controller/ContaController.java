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

    // Componentes FXML
    @FXML private TableView<Conta> tableViewContas;
    @FXML private TableColumn<Conta, Long> columnNumero;
    @FXML private TableColumn<Conta, Cliente> columnCliente;
    @FXML private TableColumn<Conta, TipoConta> columnTipo;

    // Seção para criar nova conta
    @FXML private ComboBox<Cliente> comboBoxCliente;
    @FXML private ComboBox<TipoConta> comboBoxTipoConta;

    // Seção de detalhes e ações
    @FXML private Label labelNumeroConta;
    @FXML private Label labelSaldo;
    @FXML private Label labelClienteNome;

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
}