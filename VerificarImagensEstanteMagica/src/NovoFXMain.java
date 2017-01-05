/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author _
 */
public class NovoFXMain extends Application {

    public static String title = "Verificar Imagens Estante Mágica";

    @Override
    public void start(Stage stage) {
        reStart(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void reStart(Stage stage) {
        try {
            Parent root = FXMLLoader.load(NovoFXMain.class.getResource("Tela_Inicial.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(NovoFXMain.class.getResource("tela_inicial.css").toExternalForm());

            stage.getIcons().add(new Image(NovoFXMain.class.getResource("/imgs/icon-04-piscadela.png").toString()));
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
