package buscadorimagens.controllers;

import buscadorimagens.dao.DB_DAO;
import buscadorimagens.dao.ImagensObj;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import static buscadorimagens.BuscadorImagens.addGridRows_Escolas;
import static buscadorimagens.BuscadorImagens.addTextArea_Tab_Total;
import static buscadorimagens.BuscadorImagens.mostrarAlerta;
import static buscadorimagens.BuscadorImagens.restringirPastas;
import buscadorimagens.dao.ImgsObjsAbaTotal_PorCategoria;
import buscadorimagens.dao.Listas_Padrao;
import buscadorimagens.dao.ToolBar_MenuItens;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;

/**
 * Controller da Tela Inicial (Busca por Escolas)
 *
 * @author RenanFucci
 */
public class TelaInicialController extends ToolBar_MenuItens implements Initializable {

    @FXML
    private GridPane mGridPane;

    @FXML
    private BorderPane mainPane_bordepane;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private Button mBtn_Buscar, mMenuItem_EditBD, mMenuItem_EditFTP,
            mMenuItem_Escola, mMenuItem_Crianca, mMenuItem_XML;

    @FXML
    private Tab mTab_ForaLimite, mTab_Rec, mTab_CriancaInativa, mTab_Branco, mTab_NaoRec, mTab_DelArqs,
            mTab_MudancaDono, mTab_Total;

    @FXML
    private GridPane mGrid_ForaLimite, mGrid_Rec, mGrid_CriancaInativa, mGrid_Branco, mGrid_NaoRec, mGrid_DelArqs,
            mGrid_MudancaDono, mGrid_Total;

    @FXML
    private TextField mTextField_nomeEscola, mTextField_codEscola;

    @FXML
    private Label mLbl_ou;
    @FXML
    private TabPane mTapPane;
    @FXML
    private ImageView mImageView_load;

    private TextArea lbl_url_in, lbl_url_out, lbl_date_out;
    private DB_DAO db_dao;
    private String task_alerta_title, task_alerta_headertext,
            task_alerta_contentText;
    private Alert.AlertType task_alerta_alertType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mImageView_load.setVisible(false);
        mainScrollPane.setFitToHeight(true);
        mainScrollPane.setFitToWidth(true);
        mTextField_codEscola.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                mTextField_codEscola.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        mTextField_codEscola.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                alimentarTabs_Escola();
            }
        });
        mTextField_codEscola.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                mTextField_nomeEscola.setText("");
            }
        });
        mTextField_nomeEscola.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                mTextField_codEscola.setText("");
            }

        });
        mTextField_nomeEscola.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                alimentarTabs_Escola();
            }
        });

        mBtn_Buscar.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                alimentarTabs_Escola();
            }
        });

        mBtn_Buscar.setOnAction((ActionEvent event) -> {
            alimentarTabs_Escola();
        });
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método disableFields</strong><br>
     * Método para desabilitar todos os componentes da GUI desta tela.<br>
     * @param bool booleano para desativar (true), ativar (false);
     */
    public void disableFields(boolean bool) {
        mBtn_Buscar.setDisable(bool);
        mBtn_Buscar.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTextField_codEscola.setDisable(bool);
        mTextField_codEscola.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTextField_nomeEscola.setDisable(bool);
        mTextField_nomeEscola.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTapPane.setDisable(bool);
        mTapPane.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mLbl_ou.setDisable(bool);
        mLbl_ou.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
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

    /**
     * @author RenanFucci<br>
     * <strong>Método alimentarTabs_Escolas</strong><br>
     * Método para preencher as Tabs da tabela de Busca por Escola com nome da
     * escola do ChoiceBox da GUI desta tela, data de registro do DataPicker e a
     * extensão do arquivo do ChoiceBox de tipos de arquivos.<br>
     */
    public void alimentarTabs_Escola() {
        String cod_escola = mTextField_codEscola.getText();
        String nome_escola = mTextField_nomeEscola.getText();
        if (cod_escola.contentEquals("") && nome_escola.contentEquals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Campos Vazios!");
            alert.setHeaderText("Você deve inserir pelo meno um dos campos.");
            alert.showAndWait();
            return;
        } else if (!cod_escola.contentEquals("") && !nome_escola.contentEquals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Campos Redundantes!");
            alert.setHeaderText(null);
            alert.setContentText("Preencha somente o nome ou código da escola.\nNão busque pelos dois campos.");
            alert.showAndWait();
            return;
        }
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                Tab[] tabs = {mTab_NaoRec, mTab_ForaLimite, mTab_Rec,
                    mTab_CriancaInativa, mTab_Branco, mTab_DelArqs, mTab_MudancaDono};
                GridPane[] grids = {mGrid_NaoRec, mGrid_ForaLimite, mGrid_Rec,
                    mGrid_CriancaInativa, mGrid_Branco, mGrid_DelArqs, mGrid_MudancaDono};
                ArrayList<ImagensObj> imagensObjs = new ArrayList<>();
                ArrayList<ImgsObjsAbaTotal_PorCategoria> imgsObjsAbaTotal_PorCategorias = new ArrayList<>();
                boolean avo;

                db_dao = new DB_DAO();
                db_dao.connect();

                if (!cod_escola.contentEquals("")
                        && nome_escola.contentEquals("")) {//busca por cod_escola
                    int cod = Integer.parseInt(cod_escola);
                    System.out.print("\ncodEscola:\t" + cod);
                    for (int i = 0; i < tabs.length; i++) {
                        avo = false;
                        imagensObjs = restringirPastas((db_dao.getImagens_lastStage_like_codEscola(Listas_Padrao.LIST_LAST_STAGE_SELECT[i], cod)), avo);
                        if (db_dao.isErroEhIdInvalido() || imagensObjs == null) {
                            task_alerta_title = "Banco de Dados";
                            task_alerta_headertext = "Código inválido";
                            task_alerta_contentText = "Este código não existe ou está inativo.";
                            task_alerta_alertType = Alert.AlertType.WARNING;
                            cancel();
                        }
                        insertDataInsideAllTabs(Listas_Padrao.LIST_LAST_STAGES_NOMECATEGORIAS[i], tabs[i], grids[i], imagensObjs, imgsObjsAbaTotal_PorCategorias);
                    }
                } else { //Busca por nome escola
                    for (int i = 0; i < tabs.length; i++) {
                        avo = false;
                        imagensObjs = restringirPastas((db_dao.getImagens_lastStage_like_nomeEscola(Listas_Padrao.LIST_LAST_STAGE_SELECT[i], nome_escola)), avo);
                        if (db_dao.isErroEhIdInvalido() || imagensObjs == null) {
                            task_alerta_title = "Banco de Dados";
                            task_alerta_headertext = "Nome inválido";
                            task_alerta_contentText = "Este nome não existe ou está inativo.";
                            task_alerta_alertType = Alert.AlertType.WARNING;
                            cancel();
                        }
                        insertDataInsideAllTabs(Listas_Padrao.LIST_LAST_STAGES_NOMECATEGORIAS[i], tabs[i], grids[i], imagensObjs, imgsObjsAbaTotal_PorCategorias);
                    }
                }
                Platform.runLater(() -> {
                    addTextArea_Tab_Total(mGrid_Total, mTab_Total, imgsObjsAbaTotal_PorCategorias);
                });
                return true;
            }
        };
        task.setOnRunning((Handler) -> {
            Platform.runLater(() -> {
                startLoadingIcon();
            });
        });
        task.setOnSucceeded((handler) -> {
            Platform.runLater(() -> {
                stopLoadingIcon();
            });
        });
        task.setOnCancelled((handler) -> {
            Platform.runLater(() -> {
                stopLoadingIcon();
            });
            mostrarAlerta(task_alerta_title, task_alerta_headertext, task_alerta_contentText, task_alerta_alertType);
        });
        task.setOnFailed((handler) -> {
            Platform.runLater(() -> {
                stopLoadingIcon();
            });
        });
        new Thread(task).start();

    }

    /**
     * @author RenanFucci<br>
     * <strong>Método insertDataInsideAllTabs</strong><br>
     * Método para inserir linhas e tabs, dinamicamente e com base no retorno do
     * método {@link #alimentarTabs_Escola()  alimentarTabs_Escola()}, na tabela
     * desta tela.<br>
     * @param last_stages_nomeCategoria nome da categoria da tab;
     * @param imagensObjs lista de imagens que preencherão o conteúdo das
     * linhas;
     * @param tab objeto tab, relativo ao nome da categoria;
     * @param grid tabela da tela onde serão inseridos as tabs e linhas;
     * @param imgsObjsAbaTotal_PorCategorias lista do resumo de por categoria
     * que irá preencher a tab de categoria "Total".
     */
    private void insertDataInsideAllTabs(String last_stages_nomeCategoria, Tab tab, GridPane grid, ArrayList<ImagensObj> imagensObjs, ArrayList<ImgsObjsAbaTotal_PorCategoria> imgsObjsAbaTotal_PorCategorias) {
        String nomeCategoria = last_stages_nomeCategoria;
        Tab tab_axu_final = tab;
        GridPane grid_aux_final = grid;
        String nome_aba = nomeCategoria + " (" + imagensObjs.size() + ")";
        Platform.runLater(() -> {
            tab_axu_final.setText(nome_aba);
            grid_aux_final.setGridLinesVisible(false);
            ImgsObjsAbaTotal_PorCategoria param_final = addGridRows_Escolas(imagensObjs, grid, lbl_url_in, lbl_url_out, lbl_date_out, nomeCategoria);
            imgsObjsAbaTotal_PorCategorias.add(param_final);
        });
    }

}
