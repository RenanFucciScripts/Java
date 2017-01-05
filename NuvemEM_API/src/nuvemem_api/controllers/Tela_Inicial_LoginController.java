/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nuvemem_api.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nuvemem_api.NuvemEM_API;

/**
 * FXML Controller class
 *
 * @author _
 */
public class Tela_Inicial_LoginController implements Initializable {

    @FXML
    private Button buttonEntrar;

    @FXML
    private TextField textFieldUsuario;

    @FXML
    private PasswordField passwordFieldSenha;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void onActionButtonEntrar() {
        String usuario = textFieldUsuario.getText();
        String senha = passwordFieldSenha.getText().hashCode() + "";

        Stage stage = (Stage) textFieldUsuario.getScene().getWindow();
        if (true) {
            Tela_PrincipalController controller = new Tela_PrincipalController(usuario);
            URL url_fxml = getClass().getResource("/nuvemem_api/views/Tela_Principal.fxml");
            URL url_css = getClass().getResource("/nuvemem_api/views/css/tela_principal.css");
            setScene(url_fxml, url_css, stage, controller);

        }
    }

    public void setScene(URL url_fxml, URL url_css, Stage stage, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(NuvemEM_API.title);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
