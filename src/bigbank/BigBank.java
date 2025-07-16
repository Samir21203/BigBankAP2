package bigbank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Constantes;


public class BigBank extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Carrega a interface gráfica definida no arquivo login.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        
        // 2. Cria uma "cena" com o conteúdo da interface carregada
        Scene scene = new Scene(root);
        
        // 3. Configura a janela principal (o "palco")
        primaryStage.setTitle(Constantes.TITULO_TELA_LOGIN); // Define o título da janela
        primaryStage.setScene(scene); // Coloca a cena na janela
        primaryStage.setResizable(false); // Impede que o usuário redimensione a janela de login
        
        // 4. Mostra a janela para o usuário
        primaryStage.show();
    }

    /**
     * O método main, ponto de entrada padrão para qualquer aplicação Java.
     * Ele apenas chama o launch() para iniciar o ciclo de vida do JavaFX.
     * @param args os argumentos da linha de comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}