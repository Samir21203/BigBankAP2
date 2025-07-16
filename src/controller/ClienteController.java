package controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Cliente;
import persistence.ClienteDAO;
import util.Validador;

public class ClienteController implements Initializable {

    // Componentes da Tabela
    @FXML private TableView<Cliente> tableViewClientes;
    @FXML private TableColumn<Cliente, Long> idColumnCliente;
    @FXML private TableColumn<Cliente, String> clienteColumnCliente;

    // Componentes do Formulário
    @FXML private TextField textFieldNome;
    @FXML private TextField textFieldCpf;
    @FXML private DatePicker datePickerNascimento;
    @FXML private TextField textFieldEndereco;
    @FXML private TextField textFieldTelefone;
    @FXML private TextField textFieldEmail;
    @FXML private PasswordField passwordFieldSenha;

    // Botões
    @FXML private Button buttonNewCliente;
    @FXML private Button buttonDeleteCliente;
    @FXML private Button buttonSaveCliente;
    
    private Cliente clienteSelecionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumnCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        clienteColumnCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> preencherFormulario(newValue)
        );
        
        carregarTabelaClientes();
    }    

    @FXML
    private void handleNew(ActionEvent event) {
        limparFormulario();
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (clienteSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum cliente selecionado", "Por favor, selecione um cliente na tabela para excluir.");
            return;
        }
        
        Optional<ButtonType> resultado = mostrarAlertaConfirmacao("Confirmar Exclusão", 
                "Tem certeza que deseja excluir o cliente " + clienteSelecionado.getNome() + "?");
        
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            ClienteDAO.getInstance().delete(clienteSelecionado);
            carregarTabelaClientes();
            limparFormulario();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Cliente excluído com sucesso!");
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        
        boolean isNovoCliente = (clienteSelecionado == null);
        
        if (isNovoCliente) {
           
            Cliente novoCliente = new Cliente(textFieldNome.getText(), textFieldCpf.getText());

            novoCliente.setDataNascimento(datePickerNascimento.getValue());
            novoCliente.setEndereco(textFieldEndereco.getText());
            novoCliente.setTelefone(textFieldTelefone.getText());
            novoCliente.setEmail(textFieldEmail.getText());
            novoCliente.setSenha(passwordFieldSenha.getText());
            
            ClienteDAO.getInstance().add(novoCliente);
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Novo cliente cadastrado com sucesso!");
        } else { 
            
            clienteSelecionado.setNome(textFieldNome.getText());
            
            clienteSelecionado.setDataNascimento(datePickerNascimento.getValue());
            clienteSelecionado.setEndereco(textFieldEndereco.getText());
            clienteSelecionado.setTelefone(textFieldTelefone.getText());
            clienteSelecionado.setEmail(textFieldEmail.getText());
            clienteSelecionado.setSenha(passwordFieldSenha.getText());

            ClienteDAO.getInstance().update(clienteSelecionado);
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Cliente atualizado com sucesso!");
        }

        carregarTabelaClientes();
        limparFormulario();
    }
    
    private void carregarTabelaClientes() {
        List<Cliente> clientes = ClienteDAO.getInstance().getAll();
        ObservableList<Cliente> obsClientes = FXCollections.observableArrayList(clientes);
        tableViewClientes.setItems(obsClientes);
    }
    
    private void preencherFormulario(Cliente cliente) {
        clienteSelecionado = cliente;
        if (cliente != null) {
            textFieldNome.setText(cliente.getNome());
            textFieldCpf.setText(cliente.getCpf());
            datePickerNascimento.setValue(cliente.getDataNascimento());
            textFieldEndereco.setText(cliente.getEndereco());
            textFieldTelefone.setText(cliente.getTelefone());
            textFieldEmail.setText(cliente.getEmail());
            passwordFieldSenha.setText(cliente.getSenha());
            
            textFieldCpf.setEditable(false);
        } else {
            limparFormulario();
        }
    }
    
    private void limparFormulario() {
        clienteSelecionado = null;
        textFieldNome.clear();
        textFieldCpf.clear();
        datePickerNascimento.setValue(null);
        textFieldEndereco.clear();
        textFieldTelefone.clear();
        textFieldEmail.clear();
        passwordFieldSenha.clear();
        tableViewClientes.getSelectionModel().clearSelection();
        
        textFieldCpf.setEditable(true);
    }
    
    private boolean validarCampos() {
        String nome = textFieldNome.getText();
        String cpf = textFieldCpf.getText();
        String email = textFieldEmail.getText();
        String senha = passwordFieldSenha.getText();
        
        if (Validador.isCampoVazio(nome) || Validador.isCampoVazio(cpf) || Validador.isCampoVazio(email) || Validador.isCampoVazio(senha) || datePickerNascimento.getValue() == null) {
            mostrarAlerta(AlertType.ERROR, "Erro de Validação", "Todos os campos do formulário são obrigatórios.");
            return false;
        }
        
        if (!Validador.isCpfValido(cpf)) {
            mostrarAlerta(AlertType.ERROR, "Erro de Validação", "O CPF informado é inválido.");
            return false;
        }

        if (clienteSelecionado == null && ClienteDAO.getInstance().getByCpf(cpf) != null) {
            mostrarAlerta(AlertType.ERROR, "Erro de Validação", "Já existe um cliente cadastrado com este CPF.");
            return false;
        }
        
        if (!Validador.isEmailValido(email)) {
            mostrarAlerta(AlertType.ERROR, "Erro de Validação", "O formato do e-mail é inválido.");
            return false;
        }
        return true;
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

    public void initData(Cliente cliente) {
        if (cliente != null) {
            preencherFormulario(cliente);
        }
    }
}