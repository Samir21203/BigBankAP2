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
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle(Constantes.TITULO_TELA_LOGIN);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}