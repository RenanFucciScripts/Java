/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscadorimagens.controllers;

import buscadorimagens.BuscadorImagens;
import static buscadorimagens.BuscadorImagens.addTextArea_Tab_Total;
import static buscadorimagens.BuscadorImagens.mostrarAlerta;
import static buscadorimagens.BuscadorImagens.restringirPastas;
import buscadorimagens.dao.DB_DAO;
import buscadorimagens.dao.FTP;
import buscadorimagens.dao.ImagensObj;
import buscadorimagens.dao.ImgsObjsAbaTotal_PorCategoria;
import buscadorimagens.dao.Listas_Padrao;
import buscadorimagens.dao.ToolBar_MenuItens;
import com.google.gson.JsonObject;
import java.beans.Visibility;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Controller da Tela Editar Configuração do FTP
 *
 * @author RenanFucci
 */
public class EditarConfigFTPController extends ToolBar_MenuItens implements Initializable {

    @FXML
    private GridPane mGridPane;

    @FXML
    private Button mBtn_salvar;

    @FXML
    private TextField mTxtField_Port, mTxtField_User, mTxtField_IPhost, mTxtField_root_folder;

    @FXML
    private PasswordField mPwdField_pwd;
    @FXML
    private ImageView mImageView_load;

    private String task_alerta_title, task_alerta_headertext, task_alerta_contentText;
    private Alert.AlertType task_alerta_alertType;
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JsonObject jsonObject = FTP.getConfigFTP();
        if (jsonObject != null && !jsonObject.isJsonNull()) {
            mTxtField_IPhost.setText(((jsonObject.has("iphost")) ? jsonObject.get("iphost").getAsString() : ""));
            mTxtField_root_folder.setText(((jsonObject.has("root_folder")) ? jsonObject.get("root_folder").getAsString() : ""));
            mTxtField_User.setText(((jsonObject.has("user")) ? jsonObject.get("user").getAsString() : ""));
            mPwdField_pwd.setText(((jsonObject.has("pwd")) ? jsonObject.get("pwd").getAsString() : ""));
            mTxtField_Port.setText(((jsonObject.has("port")) ? jsonObject.get("port").getAsString() : ""));
        }
        mBtn_salvar.setOnAction((ActionEvent event) -> {
            String iphost = mTxtField_IPhost.getText();
            String root_folder = mTxtField_root_folder.getText();
            String user = mTxtField_User.getText();
            String pwd = mPwdField_pwd.getText();
            String port = mTxtField_Port.getText();

            if (user.contentEquals("") || iphost.contentEquals("") /*|| pwd.contentEquals("")*/) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Campos Vazios!");
                alert.setHeaderText("Você deve inserir o IP, Usuário e Senha do FTP.");
                alert.showAndWait();

            } else {
                root_folder = (root_folder.contentEquals("")) ? ("/") : (root_folder);
                FTP.setConfigFTP(user, iphost, root_folder, pwd, port);

                Task<Boolean> task1 = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        if (FTP.testFTP_con(FTP.getConfigFTP())) {
                            stage = (Stage) ((mGridPane.getScene()).getWindow());
                            task_alerta_title = "FTP";
                            task_alerta_headertext = "Configurações são válidas e o teste de conexão foi bem sucedido.";
                            task_alerta_contentText = null;
                            task_alerta_alertType = Alert.AlertType.INFORMATION;
                        } else {
                            task_alerta_title = "FTP";
                            task_alerta_headertext = "Conexão Falhou :(";
                            task_alerta_contentText = "As informações para FTP não são válidas ou está sem acesso à internet. "
                                    + "\nSe tem certeza que as configurações são válidas e que há acesso à internet, "
                                    + "procure saber se o servidor bloqueou acesso externo.";
                            task_alerta_alertType = Alert.AlertType.ERROR;
                        }
                        return true;
                    }
                };

                task1.setOnRunning((Handler) -> {
                    Platform.runLater(() -> {
                        startLoadingIcon();
                    });
                });
                task1.setOnSucceeded((handler) -> {
                    Platform.runLater(() -> {
                        stopLoadingIcon();
                        mostrarAlerta(task_alerta_title, task_alerta_headertext, task_alerta_contentText, task_alerta_alertType);
                        BuscadorImagens.setSceneEditFTP(stage);
                    });
                });
                task1.setOnCancelled((handler) -> {
                    Platform.runLater(() -> {
                        stopLoadingIcon();
                        mostrarAlerta(task_alerta_title, task_alerta_headertext, task_alerta_contentText, task_alerta_alertType);

                    });
                });
                task1.setOnFailed((handler) -> {
                    Platform.runLater(() -> {
                        stopLoadingIcon();
                        mostrarAlerta(task_alerta_title, task_alerta_headertext, task_alerta_contentText, task_alerta_alertType);

                    });
                });
                new Thread(task1).start();

            }
        });
        mImageView_load.setVisible(false);
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método disableFields</strong><br>
     * Método para desabilitar todos os componentes da GUI desta tela.<br>
     * @param bool booleano para desativar (true), ativar (false);
     */
    public void disableFields(boolean bool) {
        mBtn_salvar.setDisable(bool);
        mBtn_salvar.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTxtField_Port.setDisable(bool);
        mTxtField_Port.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTxtField_User.setDisable(bool);
        mTxtField_User.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTxtField_IPhost.setDisable(bool);
        mTxtField_IPhost.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTxtField_root_folder.setDisable(bool);
        mTxtField_root_folder.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mPwdField_pwd.setDisable(bool);
        mPwdField_pwd.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método startLoadingIcon</strong><br>
     * Método para iniciar os componentes da GUI para tela de load.<br>
     */
    public void startLoadingIcon() {
        disableFields(true);
        mImageView_load.setVisible(true);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }
    /**
     * @author RenanFucci<br>
     * <strong>Método stopLoadingIcon</strong><br>
     * Método para parar os componentes da GUI para tela de load.<br>
     */
    public void stopLoadingIcon() {
        disableFields(false);
        mImageView_load.setVisible(false);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

}
