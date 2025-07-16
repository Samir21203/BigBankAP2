package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private Button buttonLogin;
    @FXML private Label invalidLloginLabel;
    @FXML private PasswordField passwordFieldLogin;
    @FXML private TextField userFieldLogin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        invalidLloginLabel.setVisible(false);
    }    

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = userFieldLogin.getText();
        String password = passwordFieldLogin.getText();

        if (username.isBlank() || password.isBlank()) {
            mostrarFeedbackErro("Por favor, preencha todos os campos.");
            return;
        }

        // --- Para implementar depois ---
        if (username.equals("admin") && password.equals("admin")) {
            invalidLloginLabel.setVisible(false);
            abrirTelaPrincipal();
        } else {
            mostrarFeedbackErro("Usuário ou senha inválidos!");
        }
    }

    private void abrirTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Painel Principal - BankAP2");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) buttonLogin.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Erro Crítico");
            alerta.setHeaderText("Falha ao carregar a tela principal.");
            alerta.setContentText("Causa Provável: Verifique se o arquivo 'principal.fxml' está no local correto ou se há erros no seu 'PrincipalController'.");
            alerta.showAndWait();
        }
    }
    
    private void mostrarFeedbackErro(String mensagem) {
        invalidLloginLabel.setText(mensagem);
        invalidLloginLabel.setVisible(true);
    }
}