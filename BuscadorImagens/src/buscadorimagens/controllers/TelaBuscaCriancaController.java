package buscadorimagens.controllers;

import buscadorimagens.BuscadorImagens;
import buscadorimagens.dao.DB_DAO;
import buscadorimagens.dao.ImagensObj;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import static buscadorimagens.BuscadorImagens.addGridRows_Criancas;
import static buscadorimagens.BuscadorImagens.mostrarAlerta;
import static buscadorimagens.BuscadorImagens.setList_downloads;
import buscadorimagens.dao.FTP;
import buscadorimagens.dao.ToolBar_MenuItens;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

/**
 * Controller da Tela de Busca por Crianca
 *
 * @author RenanFucci
 */
public class TelaBuscaCriancaController extends ToolBar_MenuItens implements Initializable {

    @FXML
    private GridPane mGridPane;

    @FXML
    private TextField mTextField_codCrianca, mTextField_nomeCrianca;

    @FXML
    private Button mBtn_Buscar, mMenuItem_EditFTP, mMenuItem_EditBD,
            mMenuItem_Escola, mMenuItem_Crianca, mMenuItem_XML;

    @FXML
    private ImageView mBtn_Download_bottom, mBtn_Download_top;
    @FXML
    private Tab mTab_ImgsCrianca;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private ChoiceBox mChoicebox_year;

    @FXML
    private GridPane mGrid_ImgsCrianca;

    private TextArea lbl_url_in, lbl_url_out, lbl_date_out, lbl_last_stage;
    private CheckBox check_box_download_file;

    @FXML
    private ImageView mImageView_load;

    @FXML
    private TabPane mTapPane;

    private ArrayList<String> years;
    private DB_DAO db_dao;

    private String task_alerta_title, task_alerta_headertext,
            task_alerta_contentText;
    private Alert.AlertType task_alerta_alertType;

    public HashMap<Integer, String[]> hashMap_key_objsKeys = new HashMap<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mImageView_load.setVisible(false);
        Calendar c = Calendar.getInstance();
        int currentyear = c.get(Calendar.YEAR);
        years = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            years.add((currentyear - i) + "");
        }

        mChoicebox_year.setTooltip(new Tooltip("Selecione um ano:"));
        mChoicebox_year.setItems(FXCollections.observableArrayList(years));
        mChoicebox_year.getSelectionModel().selectFirst();
        mainScrollPane.setFitToHeight(true);
        mainScrollPane.setFitToWidth(true);

        mTextField_codCrianca.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                mTextField_codCrianca.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        mTextField_codCrianca.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                alimentarTabs_Crianca();
            }
        });
        mTextField_nomeCrianca.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                alimentarTabs_Crianca();
            }
        });

        mTextField_codCrianca.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                mTextField_nomeCrianca.setText("");
            }
        });

        mTextField_nomeCrianca.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                mTextField_codCrianca.setText("");
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

    /**
     * @author RenanFucci<br>
     * <strong>Método disableFields</strong><br>
     * Método para desabilitar todos os componentes da GUI desta tela.<br>
     * @param bool booleano para desativar (true), ativar (false);
     */
    public void disableFields(boolean bool) {
        mBtn_Buscar.setDisable(bool);
        mBtn_Buscar.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTextField_codCrianca.setDisable(bool);
        mTextField_codCrianca.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTextField_nomeCrianca.setDisable(bool);
        mTextField_nomeCrianca.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mTapPane.setDisable(bool);
        mTapPane.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mChoicebox_year.setDisable(bool);
        mChoicebox_year.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mBtn_Download_top.setDisable(bool);
        mBtn_Download_top.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
        mBtn_Download_bottom.setDisable(bool);
        mBtn_Download_bottom.setBlendMode((bool) ? (BlendMode.SCREEN) : (BlendMode.SRC_OVER));
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método onClickImageView_select_all</strong><br>
     * Método para selecionar, se estiver deselecionado, e deselecionar, se
     * estiver selecionado,todos os checkbox que estiverem dinamicamente na
     * tabela.<br>
     */
    public void onClickImageView_select_all() {
        //System.out.println("sltcAll");
        ObservableList<Node> list = mGrid_ImgsCrianca.getChildren();

        list.stream().forEach((obj) -> {
            if (obj instanceof CheckBox) {
                //System.out.println("if");
                CheckBox checkBox = (CheckBox) obj;
                boolean isSelected = checkBox.isSelected();
                checkBox.setSelected(!isSelected);
                if (checkBox.isSelected()) {
                    Scene scene = checkBox.getScene();
                    String id = checkBox.getId();
                    String id_number = id.split("-")[1];
                    //System.out.println("id:\t" + id);
                    //System.out.println("id_number:\t" + id_number);
                    TextArea textArea = (TextArea) scene.lookup("#url_out-" + id_number);
                    setList_downloads.add(textArea.getText());
                    //System.out.println("\nlista:");
                    setList_downloads.stream().forEach((String sa) -> {
                        //System.out.println("\n\t:" + sa);
                    });
                } else {
                    Scene scene = checkBox.getScene();
                    String id = checkBox.getId();
                    String id_number = id.split("-")[1];
                    //System.out.println("id:\t" + id);
                    //System.out.println("id_number:\t" + id_number);
                    TextArea textArea = (TextArea) scene.lookup("#url_out-" + id_number);
                    setList_downloads.remove(textArea.getText());
                    //System.out.println("\nlista:");
                    setList_downloads.stream().forEach((String sa) -> {
                        //System.out.println("\n\t:" + sa);
                    });

                }

            }
        });
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método onAction_mBtn_Download</strong><br>
     * Método para selecionar uma pasta e baixar os arquivos selecionados dentro
     * desta pasta selecionada.<br>
     * @param event Event do click do item no menu;
     */
    public void onAction_mBtn_Download(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(getUltimoDirDownload()));
        directoryChooser.setTitle("Selecionar a pasta onde serão baixados os arquivo.");
        directoryChooser.setInitialDirectory(new File(getUltimoDirDownload()));
        File folder = directoryChooser.showDialog(null);

        if (folder != null && folder.exists()) {
            String lastforlder = folder.getAbsolutePath();
            //System.out.println("lastfolder:\t" + lastforlder);
            setUltimoDirDownload(lastforlder);
            if (folder.canWrite()) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            JsonObject jsonObject = FTP.getConfigFTP();
                            File folder_download = new File(getUltimoDirDownload());
                            //System.out.println("static.SetList:\t" + BuscadorImagens.setList_downloads.toString());
                            BuscadorImagens.setList_downloads.stream().forEach((url_out) -> {
                                //System.out.println("url_out:\t" + url_out);
                                FTP.downloadFiles(jsonObject, url_out, folder_download);
                            });
                            BuscadorImagens.setList_downloads.clear();
                            task_alerta_title = "Transferência de Dados";
                            task_alerta_headertext = "Download dos arquivos foi bem sucedido!";
                            task_alerta_contentText = null;
                            task_alerta_alertType = Alert.AlertType.INFORMATION;

                        } catch (Exception ex) {
                            System.err.println(ex.getLocalizedMessage());
                            ex.printStackTrace();
                        }
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
                    mostrarAlerta(task_alerta_title, task_alerta_headertext, task_alerta_contentText, task_alerta_alertType);
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

            } else {
                BuscadorImagens.mostrarAlerta("Arquivo", "Pasta não tem permissão de escrita.", null, Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setUltimoDirDownload</strong><br>
     * Método para setar o último diretorio selecionado na {@link #onAction_mBtn_Download(javafx.event.ActionEvent)
     * onAction_mBtn_Download()} num arquivo JSON no caminho
     * "ultimoDirDownload.json".<br>
     * @param dir String do diretório selecionada;
     */
    public static void setUltimoDirDownload(String dir) {
        final String dir_padrao = "ultimoDirDownload.json";
        try (PrintWriter fw = new PrintWriter(dir_padrao);
                BufferedWriter bw = new BufferedWriter(fw);) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dir", dir);
            bw.write(jsonObject.toString());
        } catch (Exception ex) {
            Logger.getLogger(FTP.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getUltimoDirDownload</strong><br>
     * Método para pegar o último diretorio selecionado na {@link #onAction_mBtn_Download(javafx.event.ActionEvent)
     * onAction_mBtn_Download()} e setado no método
     * {@link #setUltimoDirDownload(java.lang.String) setUltimoDirDownload()}.
     * <br>
     * @return String do diretório selecionada;
     */
    public static String getUltimoDirDownload() {
        try {
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(new FileReader("ultimoDirDownload.json"));
            JsonObject jsonObject = (JsonObject) obj;
            String str_dir = jsonObject.get("dir").getAsString();
            if (str_dir != null && new File(str_dir).exists() && new File(str_dir).isDirectory()) {
                return str_dir;
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return System.getProperty("user.home");
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
        String cod_crianca = mTextField_codCrianca.getText();
        String nome_crianca = mTextField_nomeCrianca.getText();
        if (cod_crianca.contentEquals("") && nome_crianca.contentEquals("")) {
            BuscadorImagens.mostrarAlerta("Campos Vazios!", "Você deve inserir pelo meno um dos campos.",
                    null, Alert.AlertType.INFORMATION);
            return;
        } else if (!cod_crianca.contentEquals("") && !nome_crianca.contentEquals("")) {
            BuscadorImagens.mostrarAlerta("Campos Redundantes!", "Preencha somente o nome ou código da criança.\nNão busque pelos dois campos.",
                    null, Alert.AlertType.INFORMATION);
            return;
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                db_dao = new DB_DAO();
                if (db_dao.connect()) {
                    int year = Integer.parseInt(years.get((mChoicebox_year.getSelectionModel().getSelectedIndex())));
                    ArrayList<ImagensObj> imagensObjs;
                    if (!cod_crianca.contentEquals("")
                            && nome_crianca.contentEquals("")) {//busca por cod_escola
                        int cod = Integer.parseInt(cod_crianca);
                        imagensObjs = db_dao.getImagens_lastStage_like_codCrianca(cod, year);
                        if (db_dao.isErroEhIdInvalido() || imagensObjs == null) {
                            task_alerta_title = "Banco de Dados";
                            task_alerta_headertext = "Código inválido";
                            task_alerta_contentText = "Este código não existe ou está inativo.";
                            task_alerta_alertType = Alert.AlertType.WARNING;
                            cancel();
                        }
                        insertDataInsideAllTabs_crianca(imagensObjs);
                    } else { //busca por nome_escola
                        imagensObjs = db_dao.getImagens_lastStage_like_nomeCrianca(nome_crianca, year);
                        if (db_dao.isErroEhIdInvalido() || imagensObjs == null) {
                            task_alerta_title = "Banco de Dados";
                            task_alerta_headertext = "Nome inválido";
                            task_alerta_contentText = "Este nome não existe ou está inativo.";
                            task_alerta_alertType = Alert.AlertType.WARNING;
                            cancel();
                        }
                        insertDataInsideAllTabs_crianca(imagensObjs);
                    }
                } else {
                    task_alerta_title = db_dao.task_alerta_title;
                    task_alerta_headertext = db_dao.task_alerta_headertext;
                    task_alerta_contentText = db_dao.task_alerta_contentText;
                    task_alerta_alertType = db_dao.task_alerta_alertType;
                    cancel();
                }
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
     * <strong>Método insertDataInsideAllTabs_crianca</strong><br>
     * Método para inserir linhas, dinamicamente e com base no retorno do método
     * {@link #alimentarTabs_Crianca() alimentarTabs_Crianca()}, na tabela desta
     * tela.<br>
     * @param {@literal ArrayList<ImagensObj>} lista de ImagensObj que serão
     * utilizados para inserir o conteúdo nas linhas.
     */
    private void insertDataInsideAllTabs_crianca(ArrayList<ImagensObj> imagensObjs) {
        Platform.runLater(() -> {
            if (imagensObjs != null) {
                mTab_ImgsCrianca.setText("Imagens da Criança (" + imagensObjs.size() + ")");
                addGridRows_Criancas(imagensObjs, mGrid_ImgsCrianca, lbl_url_in,
                        lbl_url_out, lbl_date_out, lbl_last_stage, check_box_download_file);
            } else {
                mTab_ImgsCrianca.setText("Imagens da Criança (" + 0 + ")");
            }
        });
    }
}
