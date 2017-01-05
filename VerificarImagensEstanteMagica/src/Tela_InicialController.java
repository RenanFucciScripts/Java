/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import models.Itens_Erro;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

/**
 * FXML Controller class
 *
 * @author _
 */
public class Tela_InicialController implements Initializable {

    @FXML
    private AnchorPane anchorPanePrincipal;
    @FXML
    private Button buttonSelecionar;
    @FXML
    private BorderPane borderPaneCenter;
    @FXML
    private ScrollPane scrollPaneMain;

    private final int limitImgs = 50;
    public static String title = "Verificar Imagens Estante Mágica";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void onClickButtonSelecionarPastas() {
        abrirChoooser_pastas();
    }

    @FXML
    public void onClickButtonSelecionarArquivos() {
        abrirChooser_arquivos();
    }

    public void setScene(URL url_fxml, URL url_css, Stage stage, ArrayList<Itens_Erro> array_erros) {
        try {
            Tela_NegativaController tela_NegativaController = new Tela_NegativaController(stage, array_erros);
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(tela_NegativaController);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(title);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    private void setScene_telaLoad(ArrayList<File> listFiles) {
        try {
            URL url_fxml = getClass().getResource("Tela_Load.fxml");
            URL url_css = getClass().getResource("tela_load.css");
            Tela_LoadController tela_LoadController = new Tela_LoadController(listFiles.size());
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(tela_LoadController);

            Stage stage = (Stage) ((borderPaneCenter.getScene()).getWindow());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(title);
            stage.setOnCloseRequest(e -> Platform.exit());

            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    doSomething(stage, listFiles, tela_LoadController);
                    return null;
                }
            };
            new Thread(task).start();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    public void setScene(URL url_fxml, URL url_css, Stage stage) {
        try {
            Parent root = FXMLLoader.load(url_fxml);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());

            stage.setTitle(title);
            stage.setOnCloseRequest(e -> Platform.exit());

            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

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

    public String getUltimoDir() {
        try {
            JsonParser parser = new JsonParser();

            Object obj = parser.parse(new FileReader("ultimoDir.json"));
            JsonObject jsonObject = (JsonObject) obj;
            String str_dir = jsonObject.get("dir").getAsString();
            if (str_dir != null && new File(str_dir).exists() && new File(str_dir).isDirectory()) {
                return str_dir;
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);

        }
        return System.getProperty("user.home");

    }

    private void doSomething(Stage stage, List<File> listFiles, Tela_LoadController tela_LoadController) {

        V_e_V_ImagensEM v_e_V_ImagensEM = new V_e_V_ImagensEM();
        try {
            boolean imgsIsOk;
            imgsIsOk = v_e_V_ImagensEM.VV_Imagens(listFiles, limitImgs, tela_LoadController);
            if (imgsIsOk && v_e_V_ImagensEM != null) {
                URL url_fxml = getClass().getResource("Tela_Positiva.fxml");
                URL url_css = getClass().getResource("tela_positiva.css");
                Platform.runLater(() -> {
                    setScene(url_fxml, url_css, stage);
                });
            } else {
                URL url_fxml = getClass().getResource("Tela_Negativa.fxml");
                URL url_css = getClass().getResource("tela_negativa.css");
                Platform.runLater(() -> {
                    setScene(url_fxml, url_css, stage, v_e_V_ImagensEM.getArray_erros());
                });
            }
        } catch (Exception ex) {
            criarTxtErro(ex);
            telaErroDesconhecido(stage);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } catch (Error er) {
            criarTxtErro(er);
            telaErroDesconhecido(stage);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, er.getLocalizedMessage(), er);
        }
    }

    private void telaErroDesconhecido(Stage stage) {
        ArrayList<Itens_Erro> arrayList_ItensErro = new ArrayList<>();
        Itens_Erro itenErro = new Itens_Erro(":(");
        itenErro.addMsgErro(V_e_V_ImagensEM.MSG_ERRO_DESCONHECIDO);
        arrayList_ItensErro.add(itenErro);
        URL url_fxml = getClass().getResource("Tela_Negativa.fxml");
        URL url_css = getClass().getResource("tela_negativa.css");
        setScene(url_fxml, url_css, stage, arrayList_ItensErro);
    }

    public static void criarTxtErro(Exception ex) {
        try {
            String str_tempo = ex.getMessage() + "\n\n" + ex.getLocalizedMessage() + "\n\n";
            PrintWriter writer = new PrintWriter("Erro-Desconhecido.txt", "UTF-8");
            writer.println(str_tempo);
            ex.printStackTrace(writer);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex1) {
            Logger.getLogger(Tela_InicialController.class.getName()).log(Level.SEVERE, ex1.getLocalizedMessage(), ex1);
        }
    }

    public static void criarTxtErro(Error ex) {
        try {
            String str_tempo = ex.getMessage() + "\n\n" + ex.getLocalizedMessage() + "\n\n";
            PrintWriter writer = new PrintWriter("Erro-Desconhecido.txt", "UTF-8");
            writer.println(str_tempo);
            ex.printStackTrace(writer);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex1) {
            Logger.getLogger(Tela_InicialController.class.getName()).log(Level.SEVERE, ex1.getLocalizedMessage(), ex1);
        }
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo criarListaArqs</b><br>
     * Metodo para criar uma lista de arquivos a partir de um diretorio.
     *
     * @param folder diretorio dos arquivos.
     * @param listaArq lista onde serao armazenados os arquivos.
     */
    private void criarListaArqs(File folder, ArrayList<File> listaArq) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                criarListaArqs(fileEntry, listaArq);
            } else if (fileIsComplete(fileEntry) && ehExtensaoPermitida(fileEntry)) {
                listaArq.add(fileEntry);
            }
        }
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo fileIsComplete<br></b>
     * Metodo para testar se o arquivo esta completamente escrito.<br>
     *
     * @param file arquivo que sera testado.
     * @return resulato booleano se o arquivo esta completo.
     */
    private boolean fileIsComplete(File file) {
        RandomAccessFile stream = null;
        try {
            stream = new RandomAccessFile(file, "rw");
            return true;
        } catch (Exception e) {
            System.err.println("Arquivo " + file.getName() + " nao esta completamente escrito.");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    System.err.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean ehExtensaoPermitida(File fileEntry) {
        String arq_ext = FilenameUtils.getExtension(fileEntry.getName());//extensao do arquivo			
        if (arq_ext.equalsIgnoreCase("pdf") || arq_ext.equalsIgnoreCase("jpeg") || arq_ext.equalsIgnoreCase("jpg")
                || arq_ext.equalsIgnoreCase("png") || arq_ext.equalsIgnoreCase("bmp")) {
            return true;
        } else {
            return false;
        }
    }

    private void abrirChoooser_pastas() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecionar pastas com arquivos somente no formato PDF ou de imagens(BMP, JPG, JPEG ou PNG)");
        directoryChooser.setInitialDirectory(new File(getUltimoDir()));
        File file = directoryChooser.showDialog(null);
        if (file != null) {
            setUltimoDir(file.getParent());
            ArrayList<File> listFiles = new ArrayList<File>();
            criarListaArqs(file, listFiles);
            if (listFiles.isEmpty()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Pasta Vazia");
                alert.setHeaderText("Você selecionou uma pasta vazia.");
                alert.showAndWait();

            } //            else if (listFiles.size() > limitImgs) {
            //                Alert alert = new Alert(AlertType.INFORMATION);
            //                alert.setTitle("Limite Excedido");
            //                alert.setHeaderText("Você selecionou mais que " + limitImgs + " arquivos."
            //                        + "\nPor favor selecione um número menor.");
            //                alert.setContentText("");
            //                alert.showAndWait();
            //            }
            else {
                setScene_telaLoad(listFiles);
            }
        }
    }

    private void abrirChooser_arquivos() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Selecionar arquivos somente no formato PDF ou de imagens(BMP, JPG, JPEG ou PNG)");

        fileChooser.setInitialDirectory(new File(getUltimoDir()));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagem e PDF", new String[]{"*.pdf", "*.jpeg", "*.jpg", "*.png", "*.bmp"}),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        ArrayList<File> listFiles = new ArrayList<>();
        List<File> list = fileChooser.showOpenMultipleDialog(null);
        if (list != null) {
            listFiles.addAll(list);
            setUltimoDir(listFiles.get(0).getParent());
            if (listFiles.isEmpty()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Lista Vazia");
                alert.setHeaderText("Você não selecionou nenhum arquivo.");
                alert.showAndWait();

            } //            else if (listFiles.size() > limitImgs) {
            //                Alert alert = new Alert(AlertType.INFORMATION);
            //                alert.setTitle("Limite Excedido");
            //                alert.setHeaderText("Você selecionou mais que " + limitImgs + " arquivos."
            //                        + "\nPor favor selecione um número menor.");
            //                alert.setContentText("");
            //
            //                alert.showAndWait();
            //            } 
            else {
                setScene_telaLoad(listFiles);
            }
        }
    }
}
