/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nuvemem_api;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nuvemem_api.controllers.Tela_Inicial_LoginController;

/**
 *
 * @author _
 */
public class NuvemEM_API extends Application {

    public static String title = "Nuvem EM";

    @Override
    public void start(Stage primaryStage) {
        reStart(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void reStart(Stage stage) {
        try {
            Tela_Inicial_LoginController tela_Controller = new Tela_Inicial_LoginController();
            URL url_fxml = NuvemEM_API.class.getResource("/nuvemem_api/views/Tela_Inicial_Login.fxml");
            URL url_css = NuvemEM_API.class.getResource("/nuvemem_api/views/css/tela_inicial_login.css");
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(tela_Controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(title);
            stage.getIcons().add(new Image(NuvemEM_API.class.getResource("/nuvemem_api/imgs/icon-01.png").toString()));
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
