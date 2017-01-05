package buscadorimagens.controllers;

import buscadorimagens.BuscadorImagens;
import buscadorimagens.dao.DB_DAO;
import buscadorimagens.dao.ToolBar_MenuItens;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Controller da Tela Editar Configuração de Banco de Dados
 *
 * @author RenanFucci
 */
public class EditarConfigBDController extends ToolBar_MenuItens implements Initializable {

    @FXML
    private GridPane mGridPane;

    @FXML
    private Button mMenuItem_EditBD, mMenuItem_EditFTP,
            mMenuItem_Escola, mMenuItem_Crianca, mMenuItem_XML, mBtn_salvar;

    @FXML
    private TextField mTxtField_NomeBD, mTxtField_User, mTxtField_IPhost;

    @FXML
    private PasswordField mPwdField_pwd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        JsonObject jsonObject = DB_DAO.getJsonBD();
        if (jsonObject != null && !jsonObject.isJsonNull()) {
            mTxtField_NomeBD.setText(((jsonObject.has("nomeBD")) ? jsonObject.get("nomeBD").getAsString() : ""));
            mTxtField_IPhost.setText(((jsonObject.has("iphost")) ? jsonObject.get("iphost").getAsString() : ""));
            mTxtField_User.setText(((jsonObject.has("user")) ? jsonObject.get("user").getAsString() : ""));
            mPwdField_pwd.setText(((jsonObject.has("pwd")) ? jsonObject.get("pwd").getAsString() : ""));
        }
        mBtn_salvar.setOnAction((ActionEvent event) -> {
            String nomeBD = mTxtField_NomeBD.getText();
            String iphost = mTxtField_IPhost.getText();
            String user = mTxtField_User.getText();
            String pwd = mPwdField_pwd.getText();

            if (nomeBD.contentEquals("") || user.contentEquals("") || iphost.contentEquals("")) {
                BuscadorImagens.mostrarAlerta("Campos Vazios!", "Você deve inserir o Nome do Banco e o Usuário.",
                        null, AlertType.INFORMATION);
            } else {
                setConfigBD(nomeBD, user, iphost, pwd);
                DB_DAO db_dao = new DB_DAO();
                db_dao.getConfigBD();
                if (db_dao.connect()) {
                    BuscadorImagens.mostrarAlerta("Concluido.", "Configurações alteradas com sucesso!",
                            "As configurações de banco de dados são válidas e o teste de conexão ocorreu com sucesso!",
                            AlertType.INFORMATION);
                }
                Stage stage = (Stage) ((mGridPane.getScene()).getWindow());
                BuscadorImagens.setSceneEditBD(stage);
            }

        });
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setConfigBD</strong><br>
     * Método para setar as configurações do banco de dados num arquivo JSON no
     * caminho "config/bd.json".<br>
     * @param nomeBD nome do banco de dados;
     * @param user usuário do banco de dados;
     * @param iphost ip do host do banco de dados;
     * @param pwd senha do banco de dados;
     */
    public void setConfigBD(String nomeBD, String user, String iphost, String pwd) {
        PrintWriter fw = null;
        BufferedWriter bw = null;
        File fl = new File("config/");
        if (!fl.exists()) {
            fl.mkdir();
        }
        final String dir_padrao = "config/bd.json";
        try {

            fw = new PrintWriter(dir_padrao);
            bw = new BufferedWriter(fw);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("nomeBD", nomeBD);
            jsonObject.addProperty("iphost", iphost);
            jsonObject.addProperty("user", user);
            jsonObject.addProperty("pwd", pwd);

            bw = new BufferedWriter(fw);
            bw.write(jsonObject.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            fw.close();
        }
        fw.close();
    }

}
