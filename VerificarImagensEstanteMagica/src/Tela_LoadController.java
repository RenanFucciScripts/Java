/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author _
 */
public class Tela_LoadController implements Initializable {

    @FXML
    private ImageView imageViewLoad;

    @FXML
    private Label lblSubtotalImgs;

    private int totalImgs;
    
    public Tela_LoadController(int totalImgs) {
        this.totalImgs = totalImgs;
    }

    public void trocarLabelSubtotalImagens(int iter){
        lblSubtotalImgs.setText("Estamos analisando o arquivo "+iter+" de um total de "+totalImgs+" arquivos.");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadItens();
    }

    private void loadItens() {
        lblSubtotalImgs.setText("Estamos analisando o arquivo 0 de um total de "+totalImgs+" arquivos.");
    }
    
    
    

}
