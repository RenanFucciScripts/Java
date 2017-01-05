package buscadorimagens.controllers;

import buscadorimagens.BuscadorImagens;
import static buscadorimagens.BuscadorImagens.addGridRows_Escolas_c_id_school;
import static buscadorimagens.BuscadorImagens.addTextArea_Tab_Total;
import static buscadorimagens.BuscadorImagens.mostrarAlerta;
import static buscadorimagens.BuscadorImagens.restringirPastas;
import buscadorimagens.dao.DB_DAO;
import buscadorimagens.dao.ImagensObj;
import buscadorimagens.dao.ImgsObjsAbaTotal_PorCategoria;
import buscadorimagens.dao.Listas_Padrao;
import buscadorimagens.dao.ToolBar_MenuItens;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * Controller da Tela Busca por Arquivo
 *
 * @author RenanFucci
 */
public class TelaBuscaPorArquivoController extends ToolBar_MenuItens implements Initializable {

    @FXML
    private GridPane mGridPane;

    @FXML
    private TextField mTextField_nomeArq;

    @FXML
    private Button mBtn_Buscar, mBtn_Download, mMenuItem_EditFTP, mMenuItem_EditBD,
            mMenuItem_Escola, mMenuItem_Crianca, mMenuItem_XML;

    @FXML
    private Tab mTab_ForaLimite, mTab_Rec, mTab_CriancaInativa, mTab_Branco, mTab_NaoRec, mTab_DelArqs,
            mTab_MudancaDono, mTab_Total;

    @FXML
    private GridPane mGrid_ForaLimite, mGrid_Rec, mGrid_CriancaInativa, mGrid_Branco, mGrid_NaoRec, mGrid_DelArqs,
            mGrid_MudancaDono, mGrid_Total;

    @FXML
    private Tab mTab_ImgsCrianca;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private ChoiceBox<String> mChoiceBox_exnts;

    @FXML
    private GridPane mGrid_ImgsCrianca;

    private TextArea lbl_url_in, lbl_url_out, lbl_date_out, lbl_last_stage;

    private CheckBox check_box_download_file;

    @FXML
    private ImageView mImageView_load;

    @FXML
    private TabPane mTapPane;

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
        mChoiceBox_exnts.setTooltip(new Tooltip("Escolha uma extensão de arquivo"));
        mChoiceBox_exnts.setItems(FXCollections.observableList(Arrays.asList(Listas_Padrao.LIST_EXTENSAO)));
        mChoiceBox_exnts.getSelectionModel().selectFirst();

        mainScrollPane.setFitToHeight(true);
        mainScrollPane.setFitToWidth(true);

        mTextField_nomeArq.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                alimentarTabs_Crianca();
            }
        });

        mBtn_Buscar.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                alimentarTabs_Crianca();
            }
        });
        mBtn_Buscar.setOnAction((ActionEvent event) -> {
            alimentarTabs_Crianca();
        });
    }

    public void onAction_mBtn_Download(ActionEvent event) {
        System.out.println("btn clicked");
    }

    public void disableFields(boolean bool) {
        mBtn_Buscar.setDisable(bool);
        mBtn_Buscar.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTextField_nomeArq.setDisable(bool);
        mTextField_nomeArq.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTapPane.setDisable(bool);
        mTapPane.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mChoiceBox_exnts.setDisable(bool);
        mChoiceBox_exnts.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mBtn_Download.setDisable(bool);
        mBtn_Download.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));

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
     * <strong>Método alimentarTabs_Crianca</strong><br>
     * Método para preencher as Tabs da tabela de Busca por Criança com o código
     * ou o nome da criança.<br>
     */
    public void alimentarTabs_Crianca() {
        String nome_arq = mTextField_nomeArq.getText();
        String extn_arq = mChoiceBox_exnts.getValue();
        if (nome_arq.contentEquals("") || extn_arq.contentEquals("")) {
            BuscadorImagens.mostrarAlerta("Campos Vazios!",
                    "Você deve preencher todos os campos.", null,
                    Alert.AlertType.INFORMATION);
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

                for (int i = 0; i < tabs.length; i++) {
                    avo = false;
                    imagensObjs = restringirPastas((db_dao.getImagens_lastStage_like_flName_flExtn(Listas_Padrao.LIST_LAST_STAGE_SELECT[i], nome_arq, extn_arq)), avo);
                    insertDataInsideAllTabs_com_id_school(Listas_Padrao.LIST_LAST_STAGES_NOMECATEGORIAS[i], tabs[i], grids[i], imagensObjs, imgsObjsAbaTotal_PorCategorias);
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
     * <strong>Método insertDataInsideAllTabs_com_id_school</strong><br>
     * Método para inserir linhas, dinamicamente e com base no retorno do método
     * {@link #alimentarTabs_Crianca() alimentarTabs_Crianca()}, na tabela desta
     * tela agrupando-os pela tag "id_school".<br>
     * @param last_stages_nomeCategoria nome da categoria da tab;
     * @param imagensObjs lista de imagens que preencherão o conteúdo das
     * linhas;
     * @param tab objeto tab, relativo ao nome da categoria;
     * @param grid tabela da tela onde serão inseridos as tabs e linhas;
     * @param imgsObjsAbaTotal_PorCategorias lista do resumo de por categoria
     * que irá preencher a tab de categoria "Total".
     */
    private void insertDataInsideAllTabs_com_id_school(String last_stages_nomeCategoria, Tab tab, GridPane grid, ArrayList<ImagensObj> imagensObjs, ArrayList<ImgsObjsAbaTotal_PorCategoria> imgsObjsAbaTotal_PorCategorias) {
        String nomeCategoria = last_stages_nomeCategoria;
        Tab tab_axu_final = tab;
        GridPane grid_aux_final = grid;
        String nome_aba = nomeCategoria + " (" + imagensObjs.size() + ")";
        Platform.runLater(() -> {
            tab_axu_final.setText(nome_aba);
            grid_aux_final.setGridLinesVisible(false);
            ImgsObjsAbaTotal_PorCategoria param_final = addGridRows_Escolas_c_id_school(imagensObjs, grid, lbl_url_in, lbl_url_out, lbl_date_out, nomeCategoria);
            imgsObjsAbaTotal_PorCategorias.add(param_final);
        });
    }

}
