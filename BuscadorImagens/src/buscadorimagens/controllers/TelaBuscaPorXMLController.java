package buscadorimagens.controllers;

import static buscadorimagens.BuscadorImagens.restringirPastas;
import buscadorimagens.BuscadorImagens;
import buscadorimagens.dao.DB_DAO;
import buscadorimagens.dao.ImagensObj;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import static buscadorimagens.BuscadorImagens.addGridRows_Escolas;
import buscadorimagens.dao.ImgsObjsAbaTotal_PorCategoria;
import static buscadorimagens.BuscadorImagens.addTextArea_Tab_Total;
import static buscadorimagens.BuscadorImagens.mostrarAlerta;
import buscadorimagens.dao.AgruparPorEscolaXML;
import buscadorimagens.dao.Listas_Padrao;
import buscadorimagens.dao.ToolBar_MenuItens;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;

/**
 * Controller da Tela de Busca por XML
 *
 * @author RenanFucci
 */
public class TelaBuscaPorXMLController extends ToolBar_MenuItens implements Initializable {

    @FXML
    private GridPane mGridPane;

    @FXML
    private BorderPane mainPane_bordepane;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private DatePicker mDatePicker;

    @FXML
    private ComboBox<String> mCombobox_Escolas, mComboBox_exnts;

    @FXML
    private Button mBtn_Buscar, mBtn_ImportXML, mMenuItem_EditBD, mMenuItem_EditFTP,
            mMenuItem_Escola, mMenuItem_Crianca, mMenuItem_XML;

    @FXML
    private Tab mTab_ForaLimite, mTab_Rec, mTab_CriancaInativa, mTab_Branco, mTab_NaoRec, mTab_DelArqs,
            mTab_MudancaDono, mTab_Total;

    @FXML
    private GridPane mGrid_ForaLimite, mGrid_Rec, mGrid_CriancaInativa, mGrid_Branco, mGrid_NaoRec, mGrid_DelArqs,
            mGrid_MudancaDono, mGrid_Total;

    @FXML
    private ImageView mImageView_load;

    @FXML
    private TabPane mTapPane;
    private TextArea lbl_url_in, lbl_url_out, lbl_date_out;
    DB_DAO db_dao;
    private final String pattern = "dd-MM-yyyy";
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
    private Map<String, String> map_NomeEscolas_dataRegistro = new HashMap<>();

    private String task_alerta_title, task_alerta_headertext,
            task_alerta_contentText;
    private Alert.AlertType task_alerta_alertType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mImageView_load.setVisible(false);
        mComboBox_exnts.setTooltip(new Tooltip("Escolha uma extensão de arquivo"));
        mComboBox_exnts.setItems(FXCollections.observableList(Arrays.asList(Listas_Padrao.LIST_EXTENSAO)));
        mComboBox_exnts.getSelectionModel().selectFirst();
        try {
            mDatePicker.setConverter(new StringConverter<LocalDate>() {
                @Override
                public String toString(LocalDate date) {
                    return (date != null) ? (dtf.format(date)) : ("");
                }

                @Override
                public LocalDate fromString(String string) {
                    return (string != null && !string.isEmpty()) ? (LocalDate.parse(string, dtf)) : (null);
                }
            });

            JsonArray jsonArray = getJsonEscolas();
            if (jsonArray != null) {
                alimentarCombobox_Escolas(jsonArray);
            }

            mainScrollPane.setFitToHeight(true);
            mainScrollPane.setFitToWidth(true);

            mBtn_ImportXML.setOnAction((ActionEvent event) -> {
                onClickButtonXML();
            });
            mBtn_Buscar.setOnKeyPressed((KeyEvent ke) -> {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    alimentarTabs_Escola();
                }
            });

            mBtn_Buscar.setOnAction((ActionEvent event) -> {
                alimentarTabs_Escola();
            });
        } catch (Exception ex) {
            BuscadorImagens.mostrarAlerta_ex("Erro!", "Erro de Sistema", ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);

        }
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
        mTapPane.setDisable(bool);
        mTapPane.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mBtn_ImportXML.setDisable(bool);
        mBtn_ImportXML.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mCombobox_Escolas.setDisable(bool);
        mCombobox_Escolas.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mComboBox_exnts.setDisable(bool);
        mComboBox_exnts.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mDatePicker.setDisable(bool);
        mDatePicker.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            System.err.println(ex.getLocalizedMessage());
        }

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
     * <strong>Método onClickButtonXML</strong><br>
     * Método para abrir o Selecinador de Arquivos, para que o usuário selecione
     * o arquivo XML.<br>
     */
    public void onClickButtonXML() {
        abrirChooser_arquivos();
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getJsonEscolas</strong><br>
     * Método para pegar o arquivo JSON com a lista de escolas selecionadas do
     * arquivo XML escolhido no método {@link #onClickButtonXML()
     * onClickButtonXML()} e setado no método
     * {@link #setJsonEscolas(com.google.gson.JsonArray) setJsonEscolas()}.
     * <br>
     * @return JsonArray com a lista de escolas do JSON;
     */
    public JsonArray getJsonEscolas() {
        try (FileReader fr = new FileReader("jsonEscolas.json");) {
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(fr);
            JsonArray jsonArray = (JsonArray) obj;
            if (!jsonArray.isJsonNull() || jsonArray.isJsonArray()) {
                return jsonArray;
            }
        } catch (FileNotFoundException ex) {
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método alimentarCombobox_Escolas</strong><br>
     * Método para preencher o ChoiceBox de Escolas com base no arquivo XML.<br>
     * @param jsonArray JsonArray com as escolas que serão inseridas no
     * ChoiceBox;
     */
    private void alimentarCombobox_Escolas(JsonArray jsonArray) {
        map_NomeEscolas_dataRegistro = new HashMap<>();
        mCombobox_Escolas.setTooltip(new Tooltip("Selecione uma Escola:"));
        mCombobox_Escolas.getSelectionModel().clearSelection();
        mCombobox_Escolas.getItems().clear();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            if (i == 0) {
                list.add("Selecione uma escola:");
            }
            JsonObject jsonObj = jsonArray.get(i).getAsJsonObject();
            String str_nomescola = jsonObj.get("NomeEscola").getAsString();
            String str_dataregistro = jsonObj.get("DataRegistro").getAsString();
            map_NomeEscolas_dataRegistro.put(str_nomescola, str_dataregistro);
            list.add(str_nomescola);
        }
        mCombobox_Escolas.setItems(FXCollections.observableList(list));
        disableFields(false);
        mCombobox_Escolas.getSelectionModel().selectFirst();
        mCombobox_Escolas.setOnAction((event) -> {
            String nomeEscola = mCombobox_Escolas.getSelectionModel().getSelectedItem();
            String dataRegistro = map_NomeEscolas_dataRegistro.get(nomeEscola);
            if (nomeEscola != null && !nomeEscola.contentEquals("")
                    && dataRegistro != null && !dataRegistro.contentEquals("")) {
                mDatePicker.setValue(LocalDate.parse(dataRegistro, dtf));
            }
        });

    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setJsonEscolas</strong><br>
     * Método para setar num arquivo JSON uma lista de escola a partir de um
     * arquivo XML escolhido pelo método
     * {@link #onClickButtonXML() onClickButtonXML()}. <br>
     * @param jsonEscolas JsonArray com a lista de escolas.
     *
     */
    public void setJsonEscolas(JsonArray jsonEscolas) {
        final String dir_padrao = "jsonEscolas.json";
        try (PrintWriter fw = new PrintWriter(dir_padrao);
                BufferedWriter bw = new BufferedWriter(fw);) {
            bw.write(jsonEscolas.toString());
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setUltimoDir</strong><br>
     * Método para setar o último diretorio selecionado
     * {@link #abrirChooser_arquivos() abrirChooser_arquivos()} num arquivo JSON
     * no caminho "ultimoDir.json".<br>
     * @param dir String do diretório selecionado;
     */
    public void setUltimoDir(String dir) {
        final String dir_padrao = "ultimoDir.json";
        try (PrintWriter fw = new PrintWriter(dir_padrao);
                BufferedWriter bw = new BufferedWriter(fw);) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dir", dir);
            bw.write(jsonObject.toString());
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getUltimoDir</strong><br>
     * Método para pegar o último diretorio selecionado
     * {@link #abrirChooser_arquivos() abrirChooser_arquivos()} num arquivo JSON
     * no caminho "ultimoDir.json".<br>
     * @return String do diretório selecionado;
     */
    public String getUltimoDir() {
        try (FileReader fr = new FileReader("ultimoDir.json");) {
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(fr);
            JsonObject jsonObject = (JsonObject) obj;
            String str_dir = jsonObject.get("dir").getAsString();
            if (str_dir != null && new File(str_dir).exists() && new File(str_dir).isDirectory()) {
                return str_dir;
            }
        } catch (FileNotFoundException ex) {
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return System.getProperty("user.home");
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método abrirChooser_arquivos</strong><br>
     * Método para abrir o Selecinador de Arquivos, para que o usuário selecione
     * o arquivo XML.<br>
     */
    private void abrirChooser_arquivos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar o arquivo XML para importação.");
        fileChooser.setInitialDirectory(new File(getUltimoDir()));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Arquivo XML", new String[]{"*.xml"}),
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            setUltimoDir(file.getParent());
            if ((!file.exists()) || (!file.canRead())) {
                mostrarAlerta("Arquivo Ilegível.", "Este arquivo não existe ou está corrompido.", null, Alert.AlertType.INFORMATION);
            } else {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        AgruparPorEscolaXML escolaXML = new AgruparPorEscolaXML(file.getAbsolutePath());
                        setJsonEscolas(escolaXML.getJsonPorEscola());
                        JsonArray jsonArray = getJsonEscolas();
                        Platform.runLater(() -> {
                            alimentarCombobox_Escolas(jsonArray);
                        });
                        return null;
                    }
                };
                task.setOnRunning((handler) -> {
                    Platform.runLater(() -> {
                        startLoadingIcon();
                    });
                });
                task.setOnSucceeded((handler) -> {
                    Platform.runLater(() -> {
                        stopLoadingIcon();
                    });
                });
                new Thread(task).start();
            }
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
        String nome_escola = mCombobox_Escolas.getValue();
        LocalDate data_registro = mDatePicker.getValue();
        String extensao_arq = mComboBox_exnts.getValue();
        if (data_registro == null || data_registro.toString().contentEquals("") || extensao_arq.contentEquals("")
                || (nome_escola.contentEquals("") || nome_escola.contentEquals("Selecione uma escola:"))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Campos Vazios!");
            alert.setHeaderText("Você deve selecionar uma escola e uma data válida.");
            alert.showAndWait();
            return;
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                db_dao = new DB_DAO();
                db_dao.connect();
                ArrayList<ImagensObj> imagensObjs;
                ArrayList<ImgsObjsAbaTotal_PorCategoria> imgsObjsAbaTotal_PorCategorias = new ArrayList<>();
                String nomeCategoria = "";

                Tab[] tabs = {mTab_NaoRec, mTab_ForaLimite, mTab_Rec,
                    mTab_CriancaInativa, mTab_Branco, mTab_DelArqs, mTab_MudancaDono};
                GridPane[] grids = {mGrid_NaoRec, mGrid_ForaLimite, mGrid_Rec,
                    mGrid_CriancaInativa, mGrid_Branco, mGrid_DelArqs, mGrid_MudancaDono};
                boolean avo;
                String month_00 = ((data_registro.getMonthValue() + "").length() == 1) ? ("0" + data_registro.getMonthValue()) : (data_registro.getMonthValue() + "");
                String dataRegitro = "" + +data_registro.getDayOfMonth() + month_00 + data_registro.getYear();
                for (int i = 0; i < tabs.length; i++) {
                    avo = false;
                    imagensObjs = restringirPastas((db_dao.getImagens_contaisString_dataRegistroBARRAnomeEscola_url_in(nome_escola, dataRegitro, extensao_arq, Listas_Padrao.LIST_LAST_STAGE_SELECT[i])), avo);
                    if (db_dao.isErroEhIdInvalido() || imagensObjs == null) {
                        task_alerta_title = "Banco de Dados";
                        task_alerta_headertext = "Registro inválido";
                        task_alerta_contentText = "Este registro não existe ou está inativo.";
                        task_alerta_alertType = Alert.AlertType.WARNING;
                        cancel();
                    }
                    insertDataInsideAllTabs_XML(Listas_Padrao.LIST_LAST_STAGES_NOMECATEGORIAS[i], imagensObjs, tabs[i], grids[i], imgsObjsAbaTotal_PorCategorias);
                }
                Platform.runLater(() -> {
                    addTextArea_Tab_Total(mGrid_Total, mTab_Total, imgsObjsAbaTotal_PorCategorias);
                });
                return null;
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
     * <strong>Método insertDataInsideAllTabs_XML</strong><br>
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
    private void insertDataInsideAllTabs_XML(String last_stages_nomeCategoria,
            ArrayList<ImagensObj> imagensObjs,
            Tab tab, GridPane grid,
            ArrayList<ImgsObjsAbaTotal_PorCategoria> imgsObjsAbaTotal_PorCategorias) {
        String nomeCategoria = last_stages_nomeCategoria;
        Platform.runLater(() -> {
            tab.setText(nomeCategoria + " (" + imagensObjs.size() + ")");
            grid.setGridLinesVisible(false);
            imgsObjsAbaTotal_PorCategorias.add(addGridRows_Escolas(imagensObjs, grid, lbl_url_in, lbl_url_out, lbl_date_out, nomeCategoria));

        });
    }
}
