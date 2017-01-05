/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nuvemem_api.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author _
 */
public class Tela_PrincipalController implements Initializable {

    @FXML
    private TreeView<String> treeViewFiles;

    public String nameUser;
    private final Node rootIcon = new ImageView(new Image(Tela_PrincipalController.class.getResource("/nuvemem_api/imgs/icon-04-piscadela.png").toString()));

    public Tela_PrincipalController(String user) {
        this.nameUser = user;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("...");
    }

}
