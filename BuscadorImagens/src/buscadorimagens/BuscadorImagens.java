package buscadorimagens;

import buscadorimagens.controllers.EditarConfigBDController;
import buscadorimagens.controllers.EditarConfigFTPController;
import buscadorimagens.controllers.TelaBuscaCriancaController;
import buscadorimagens.controllers.TelaBuscaPorArquivoController;
import buscadorimagens.controllers.TelaBuscaPorXMLController;
import buscadorimagens.controllers.TelaInicialController;
import buscadorimagens.dao.ImagensObj;
import buscadorimagens.dao.ImgsObjsAbaTotal_PorCategoria;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Classe Principal que iniciará o BuscadorImagens
 *
 * @author RenanFucci
 */
public class BuscadorImagens extends Application {

    private static final String TITLE = "Buscador de Imagens";
    private static final int TAM_TXT_AREA = 50;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        reStart(stage);
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setSceneArquivo</strong><br>
     * Método para setar a Tela de Busca por Arquivo<br>
     * @param stage {@link Stage};
     */
    public static void setSceneArquivo(Stage stage) {
        try {
            URL url_fxml = BuscadorImagens.class.getResource("fxml/TelaBuscaPorArquivo.fxml");
            URL url_css = BuscadorImagens.class.getResource("css/telabuscaporarquivo.css");
            TelaBuscaPorArquivoController controller = new TelaBuscaPorArquivoController();
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(TITLE);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setSceneXML</strong><br>
     * Método para setar a Tela de Busca por XML<br>
     * @param stage {@link Stage};
     */
    public static void setSceneXML(Stage stage) {
        try {
            URL url_fxml = BuscadorImagens.class.getResource("fxml/TelaBuscaPorXML.fxml");
            URL url_css = BuscadorImagens.class.getResource("css/telabuscaporxml.css");
            TelaBuscaPorXMLController controller = new TelaBuscaPorXMLController();
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(TITLE);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setSceneEditFTP</strong><br>
     * Método para setar a Tela de Editar Configurações de FTP<br>
     * @param stage {@link Stage};
     */
    public static void setSceneEditFTP(Stage stage) {
        try {
            URL url_fxml = BuscadorImagens.class.getResource("fxml/EditarConfigFTP.fxml");
            URL url_css = BuscadorImagens.class.getResource("css/editarconfigftp.css");
            EditarConfigFTPController controller = new EditarConfigFTPController();
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(TITLE);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método reStart</strong><br>
     * Método para setar a Tela Inicial (Tela de Busca por Escola)<br>
     * @param stage {@link Stage};
     */
    public static void reStart(Stage stage) {
        try {
            TelaInicialController controller = new TelaInicialController();
            FXMLLoader loader = new FXMLLoader(BuscadorImagens.class.getResource("fxml/TelaInicial.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(BuscadorImagens.class.getResource("css/telainicial.css").toExternalForm());
            stage.getIcons().add(new Image(BuscadorImagens.class.getResource("imgs/icon-04-piscadela.png").toString()));
            stage.setTitle(TITLE);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setSceneEditBD</strong><br>
     * Método para setar a Tela de Editar Configurações de BD<br>
     * @param stage {@link Stage};
     */
    public static void setSceneEditBD(Stage stage) {
        try {
            URL url_fxml = BuscadorImagens.class.getResource("fxml/EditarConfigBD.fxml");
            URL url_css = BuscadorImagens.class.getResource("css/editarconfigbd.css");
            EditarConfigBDController controller = new EditarConfigBDController();
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(TITLE);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setSceneEscola</strong><br>
     * Método para setar a Tela de Busca por Escolas<br>
     * @param stage {@link Stage};
     */
    public static void setSceneEscola(Stage stage) {
        try {
            URL url_fxml = BuscadorImagens.class.getResource("fxml/TelaInicial.fxml");
            URL url_css = BuscadorImagens.class.getResource("css/telainicial.css");

            TelaInicialController controller = new TelaInicialController();
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(TITLE);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setSceneEscola</strong><br>
     * Método para setar a Tela de Busca por Criança<br>
     * @param stage {@link Stage};
     */
    public static void setSceneCrianca(Stage stage) {
        try {
            URL url_fxml = BuscadorImagens.class.getResource("fxml/TelaBuscaCrianca.fxml");
            URL url_css = BuscadorImagens.class.getResource("css/telabuscacrianca.css");

            TelaBuscaCriancaController controller = new TelaBuscaCriancaController();
            FXMLLoader loader = new FXMLLoader(url_fxml);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(url_css.toExternalForm());
            stage.setTitle(TITLE);
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception ex) {
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método mudarDataBR</strong><br>
     * Método para mudar a data do padrão 'yyyy-MM-dd HH:mm:ss.S' para o padrão
     * 'dd-MM-yyyy'<br>
     * @param dataUS String com data no padrão yyyy-MM-dd HH:mm:ss.S;
     * @return String com data no padrão 'dd-MM-yyyy'
     */
    public static String mudarDataBR(String dataUS) {
        // System.out.println(dataUS);
        String oldstring = dataUS;
        LocalDateTime datetime = LocalDateTime.parse(oldstring, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        String newstring = datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        //System.out.println(newstring); // 18-01-2011
        return newstring;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método mostrarAlerta</strong><br>
     * Método para mostrar pop-up de alerta na GUI<br>
     * @param alerta_title String do título do alerta;
     * @param alerta_headertext String do cabeçalho do alerta;
     * @param alerta_contentText String do conteúdo do alerta;
     * @param alerta_alertType AlertType do tipo do alerta, que se parametro
     * {@code alerta_contentText} for diferente de nulo, representará o icone do
     * alerta.
     */
    public static void mostrarAlerta(String alerta_title, String alerta_headertext,
            String alerta_contentText, Alert.AlertType alerta_alertType) {
        Alert alert = new Alert(alerta_alertType);
        alert.setTitle(alerta_title);
        alert.setHeaderText(alerta_headertext);
        alert.setContentText(alerta_contentText);
        alert.showAndWait();
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método mostrarAlerta_ex</strong><br>
     * Método para mostrar pop-up de alerta com a exceção lançada na GUI<br>
     * @param alerta_title String do título do alerta;
     * @param alerta_headertext String do cabeçalho do alerta;
     * @param ex Exception da exceção lançada;
     */
    public static void mostrarAlerta_ex(String alerta_title,
            String alerta_headertext, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alerta_title);
        alert.setHeaderText(alerta_headertext);
        alert.setContentText("Se possível, favor copiar o texto de erro abaixo e enviar para a equipe da Estante Mágica!");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("O erro foi:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método restringirPastas</strong><br>
     * Método para restringir somente as pastas pais do
     * {@link ImagensObj#getUrl_in() url_in} e
     * {@link ImagensObj#getUrl_out() () url_out}<br>
     * @param imagensObjs {@literal  ArrayList<ImagensObj>} que será feito a
     * restrição;
     * @param avo Boolean para pegar a pasta avô, isto é, a pasta pai da pasta
     * pai;
     * @return {@literal  ArrayList<ImagensObj>} com as restrições;
     */
    public static ArrayList<ImagensObj> restringirPastas(ArrayList<ImagensObj> imagensObjs, boolean avo) {
        ArrayList<ImagensObj> out_imagensObjs = new ArrayList<>();
        if (imagensObjs != null && !imagensObjs.isEmpty()) {
            imagensObjs.stream().forEach((in_imagensObj) -> {
                String url_in = "";
                String url_out = "";
                if (avo) {
                    url_in = (new File(in_imagensObj.getUrl_in())).getParentFile().getParent();
                    url_out = (new File(in_imagensObj.getUrl_out())).getParentFile().getParent();
                } else {
                    url_in = (new File(in_imagensObj.getUrl_in())).getParent();
                    url_out = (new File(in_imagensObj.getUrl_out())).getParent();
                }
                out_imagensObjs.add(new ImagensObj(url_in, url_out, in_imagensObj.getDate_out(), in_imagensObj.getLast_stage()));
            });
        }
        return out_imagensObjs;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método ordenarPorTag_last_stage</strong><br>
     * Método para ordenar a lista pela tag {@literal last_stage}<br>
     * @param imagensObjs {@literal ArrayList<ImagensObj>} que será ordenada;
     * @return {@literal  ArrayList<ImagensObj>} ordenada;
     */
    private static ArrayList<ImagensObj> ordenarPorTag_last_stage(
            ArrayList<ImagensObj> imagensObjs) {
        imagensObjs.sort((ImagensObj o1, ImagensObj o2)
                -> o1.getLast_stage().compareTo(o2.getLast_stage()));
        return imagensObjs;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método ordenarPorTag_id_school</strong><br>
     * Método para ordenar a lista pela tag {@literal id_school}<br>
     * @param imagensObjs {@literal ArrayList<ImagensObj>} que será ordenada;
     * @return {@literal  ArrayList<ImagensObj>} ordenada;
     */
    private static ArrayList<ImagensObj> ordenarPorTag_id_school(ArrayList<ImagensObj> imagensObjs) {
        imagensObjs.sort((ImagensObj o1, ImagensObj o2) -> Integer.compare(o1.getId_school(), o2.getId_school()));
        return imagensObjs;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Campo setList_downloads</strong><br>
     * Campo para setar a lista de itens selecionados na GUI pelos Checkbox.<br>
     */
    public static Set<String> setList_downloads = new HashSet<>();

    /**
     * @author RenanFucci<br>
     * <strong>Método addGridRows_Criancas</strong><br>
     * Método para adicionar, dinamicamente, linhas na tabela da Tela de Busca
     * por Criança.<br>
     * @param imagensObjs lista que será utilizada para prencher o conteudo dos
     * componentes gráficos que serão adicionados dinamicamente;
     * @param grid objeto da tabela onde serão inseridas as linhas;
     * @param lbl_url_in area de texto onde mostrará o conteúdo da tag url_in;
     * @param lbl_url_out area de texto onde mostrará o conteúdo da tag url_out;
     * @param lbl_date_out area de texto onde mostrara o conteúdo da tag
     * date_out;
     * @param lbl_last_stage area de texto onde mostrara o conteúdo da tag
     * last_stage;
     * @param check_box_download_file caixa de marcação para o download do
     * arquivo;
     */
    public static void addGridRows_Criancas(ArrayList<ImagensObj> imagensObjs,
            GridPane grid, TextArea lbl_url_in,
            TextArea lbl_url_out, TextArea lbl_date_out,
            TextArea lbl_last_stage, CheckBox check_box_download_file) {
        try {
            int count = 0;
            grid.getChildren().clear();
            if (!imagensObjs.isEmpty()) {
                grid.setGridLinesVisible(true);
                grid.getColumnConstraints().get(0).setPercentWidth(30);
                grid.getColumnConstraints().get(1).setPercentWidth(30);
                grid.getColumnConstraints().get(2).setPercentWidth(15);
                grid.getColumnConstraints().get(3).setPercentWidth(15);
                grid.getColumnConstraints().get(4).setPercentWidth(10);
                imagensObjs = ordenarPorTag_last_stage(imagensObjs);
                for (ImagensObj imgobj : imagensObjs) {
                    lbl_url_in = new TextArea(imgobj.getUrl_in());
                    lbl_url_in.setEditable(false);
                    lbl_url_in.setWrapText(true);
                    lbl_url_in.setPrefHeight(TAM_TXT_AREA);
                    lbl_url_in.setMinHeight(TAM_TXT_AREA);

                    lbl_url_out = new TextArea(imgobj.getUrl_out());
                    lbl_url_out.setEditable(false);
                    lbl_url_out.setWrapText(true);
                    lbl_url_out.setPrefHeight(TAM_TXT_AREA);
                    lbl_url_out.setMinHeight(TAM_TXT_AREA);
                    lbl_url_out.setId("url_out-" + count);

                    lbl_date_out = new TextArea(BuscadorImagens.mudarDataBR(imgobj.getDate_out()));
                    lbl_date_out.setEditable(false);
                    lbl_date_out.setWrapText(true);
                    lbl_date_out.setPrefHeight(TAM_TXT_AREA);
                    lbl_date_out.setMinHeight(TAM_TXT_AREA);
//                    lbl_date_out.setId("#date_out-"+count);

                    lbl_last_stage = new TextArea(imgobj.getLast_stage());
                    lbl_last_stage.setEditable(false);
                    lbl_last_stage.setWrapText(true);
                    lbl_last_stage.setPrefHeight(TAM_TXT_AREA);
                    lbl_last_stage.setMinHeight(TAM_TXT_AREA);

                    check_box_download_file = new CheckBox("\u0020\u0020\u0020\u0020\u0020");
                    check_box_download_file.setId("check_box-" + count);
                    check_box_download_file.setOnAction((ActionEvent eventHandler) -> {
                        if (eventHandler.getSource() instanceof CheckBox) {
                            CheckBox chk = (CheckBox) eventHandler.getSource();
                            if (chk.isSelected()) {
                                setList_downloads.add(imgobj.getUrl_out());
                            } else {
                                setList_downloads.remove(imgobj.getUrl_out());
                            }
                            System.out.println("\n\n");

                            setList_downloads.stream().forEach((String sa) -> {
                                System.out.println(sa);
                            });

                        }
                    });

                    Node[] children = {lbl_url_in, lbl_url_out,
                        lbl_date_out, lbl_last_stage, check_box_download_file};
                    grid.addRow(count, children);
                    GridPane.setHalignment(check_box_download_file, HPos.CENTER);
                    count += 1;
                }

            }
        } catch (Exception ex) {
            mostrarAlerta_ex("Erro!", "Erro de Sistema", ex);
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método addGridRows_Escolas_c_id_school</strong><br>
     * Método para adicionar, dinamicamente, linhas na tabela da Tela de Busca
     * por Escola. Diferentemente da {@link #addGridRows_Escolas(java.util.ArrayList,
     * javafx.scene.layout.GridPane, javafx.scene.control.TextArea,
     * javafx.scene.control.TextArea, javafx.scene.control.TextArea,
     * java.lang.String) addGridRows_Escolas()}, esse método agrupa as linhas
     * pela tag id_school.<br>
     * @param imagensObjs lista que será utilizada para prencher o conteudo dos
     * componentes gráficos que serão adicionados dinamicamente;
     * @param grid objeto da tabela onde serão inseridas as linhas;
     * @param lbl_url_in area de texto onde mostrará o conteúdo da tag url_in;
     * @param lbl_url_out area de texto onde mostrará o conteúdo da tag url_out;
     * @param lbl_date_out area de texto onde mostrara o conteúdo da tag
     * date_out;
     * @param nomeCategoria nome da categoria da aba total.
     * @return ImgsObjsAbaTotal_PorCategoria com o total das linhas criadas
     * dinamicamente.
     */
    public static ImgsObjsAbaTotal_PorCategoria addGridRows_Escolas_c_id_school(ArrayList<ImagensObj> imagensObjs, GridPane grid,
            TextArea lbl_url_in, TextArea lbl_url_out, TextArea lbl_date_out,
            String nomeCategoria) {
        ImgsObjsAbaTotal_PorCategoria total_PorCategoria
                = new ImgsObjsAbaTotal_PorCategoria();
        try {
            int count = 0;
            grid.getChildren().clear();
            if (!imagensObjs.isEmpty()) {
                imagensObjs = ordenarPorTag_id_school(imagensObjs);
                total_PorCategoria = new ImgsObjsAbaTotal_PorCategoria(nomeCategoria, imagensObjs.size());
                grid.setGridLinesVisible(true);

                for (ImagensObj imgobj : imagensObjs) {
                    String str_url_in = imgobj.getUrl_in();
                    total_PorCategoria.add_Url_in(str_url_in);
                    lbl_url_in = new TextArea(str_url_in);
                    lbl_url_in.setEditable(false);
                    lbl_url_in.setWrapText(true);
                    lbl_url_in.setPrefHeight(TAM_TXT_AREA);
                    lbl_url_in.setMinHeight(TAM_TXT_AREA);

                    String str_url_out = imgobj.getUrl_out();
                    lbl_url_out = new TextArea(str_url_out);
                    lbl_url_out.setEditable(false);
                    lbl_url_out.setWrapText(true);
                    lbl_url_out.setPrefHeight(TAM_TXT_AREA);
                    lbl_url_out.setMinHeight(TAM_TXT_AREA);

                    String str_date_out = mudarDataBR(imgobj.getDate_out());
                    lbl_date_out = new TextArea(str_date_out);
                    total_PorCategoria.add_date_out(str_date_out);
                    lbl_date_out.setEditable(false);
                    lbl_date_out.setWrapText(true);
                    lbl_date_out.setPrefHeight(TAM_TXT_AREA);
                    lbl_date_out.setMinHeight(TAM_TXT_AREA);

                    Node[] children = {lbl_url_in, lbl_url_out, lbl_date_out};
                    grid.addRow(count, children);
                    count += 1;
                }
            }
        } catch (Exception ex) {
            mostrarAlerta_ex("Erro!", "Erro de Sistema", ex);
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return total_PorCategoria;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método addGridRows_Escolas</strong><br>
     * Método para adicionar, dinamicamente, linhas na tabela da Tela de Busca
     * por Escola. Diferentemente da {@link #addGridRows_Escolas_c_id_school(java.util.ArrayList,
     * javafx.scene.layout.GridPane, javafx.scene.control.TextArea,
     * javafx.scene.control.TextArea, javafx.scene.control.TextArea,
     * java.lang.String) addGridRows_Escolas_c_id_school()}, esse método não
     * agrupa as linhas.<br>
     * @param imagensObjs lista que será utilizada para prencher o conteudo dos
     * componentes gráficos que serão adicionados dinamicamente;
     * @param grid objeto da tabela onde serão inseridas as linhas;
     * @param lbl_url_in area de texto onde mostrará o conteúdo da tag url_in;
     * @param lbl_url_out area de texto onde mostrará o conteúdo da tag url_out;
     * @param lbl_date_out area de texto onde mostrara o conteúdo da tag
     * date_out;
     * @param nomeCategoria nome da categoria da aba total.
     * @return ImgsObjsAbaTotal_PorCategoria com o total das linhas criadas
     * dinamicamente.
     */
    public static ImgsObjsAbaTotal_PorCategoria addGridRows_Escolas(ArrayList<ImagensObj> imagensObjs, GridPane grid,
            TextArea lbl_url_in, TextArea lbl_url_out, TextArea lbl_date_out, String nomeCategoria) {
        ImgsObjsAbaTotal_PorCategoria total_PorCategoria = new ImgsObjsAbaTotal_PorCategoria();
        try {
            int count = 0;
            grid.getChildren().clear();
            if (!imagensObjs.isEmpty()) {
                total_PorCategoria = new ImgsObjsAbaTotal_PorCategoria(nomeCategoria, imagensObjs.size());
                grid.setGridLinesVisible(true);
                for (ImagensObj imgobj : imagensObjs) {
                    String str_url_in = imgobj.getUrl_in();
                    total_PorCategoria.add_Url_in(str_url_in);
                    lbl_url_in = new TextArea(str_url_in);
                    lbl_url_in.setEditable(false);
                    lbl_url_in.setWrapText(true);
                    lbl_url_in.setPrefHeight(TAM_TXT_AREA);
                    lbl_url_in.setMinHeight(TAM_TXT_AREA);

                    String str_url_out = imgobj.getUrl_out();
                    lbl_url_out = new TextArea(str_url_out);
                    lbl_url_out.setEditable(false);
                    lbl_url_out.setWrapText(true);
                    lbl_url_out.setPrefHeight(TAM_TXT_AREA);
                    lbl_url_out.setMinHeight(TAM_TXT_AREA);

                    String str_date_out = mudarDataBR(imgobj.getDate_out());
                    lbl_date_out = new TextArea(str_date_out);
                    total_PorCategoria.add_date_out(str_date_out);
                    lbl_date_out.setEditable(false);
                    lbl_date_out.setWrapText(true);
                    lbl_date_out.setPrefHeight(TAM_TXT_AREA);
                    lbl_date_out.setMinHeight(TAM_TXT_AREA);

                    Node[] children = {lbl_url_in, lbl_url_out, lbl_date_out};
                    grid.addRow(count, children);
                    count += 1;
                }
            }
        } catch (Exception ex) {
            mostrarAlerta_ex("Erro!", "Erro de Sistema", ex);
            Logger.getLogger(BuscadorImagens.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return total_PorCategoria;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método addTextArea_Tab_Total</strong><br>
     * Método para adicionar, conforme resultado do método
     * {@link #addGridRows_Escolas(java.util.ArrayList, javafx.scene.layout.GridPane, javafx.scene.control.TextArea, javafx.scene.control.TextArea, javafx.scene.control.TextArea, java.lang.String) addGridRows_Escolas()}
     * ou
     * {@link #addGridRows_Escolas(java.util.ArrayList, javafx.scene.layout.GridPane, javafx.scene.control.TextArea, javafx.scene.control.TextArea, javafx.scene.control.TextArea, java.lang.String) addGridRows_Escolas_c_id_school()}
     * , uma tab com um resumo das linhas criadas por esses métodos.<br>
     * @param grid objeto da tabela onde serão inseridas as linhas;
     * @param tab onde será adicionado o resumo das linhas e outras tabs;
     * @param imgsObjsAbaTotal_PorCategorias lista que será utilizada para
     * prencher o conteudo dos componentes gráficos que mostrarão os totais;
     */
    public static void addTextArea_Tab_Total(GridPane grid, Tab tab,
            ArrayList<ImgsObjsAbaTotal_PorCategoria> imgsObjsAbaTotal_PorCategorias) {
        String textoTotal = "";
        int totalCategoria = 0;
        for (ImgsObjsAbaTotal_PorCategoria objPorCategoria : imgsObjsAbaTotal_PorCategorias) {
            if (!(objPorCategoria.isIsNull())) {
                totalCategoria += objPorCategoria.getTotalCategoria();
                textoTotal += "\n\n" + objPorCategoria.getNomeCategoria() + " (" + objPorCategoria.getTotalCategoria() + "):\n";
                for (Map.Entry<String, Integer> urlin_qntd : objPorCategoria.getMap_urlin_qntd().entrySet()) {
                    String key = urlin_qntd.getKey();
                    Integer value = urlin_qntd.getValue();
                    textoTotal += "\t- " + value + " na pasta " + key + ";\n";
                }
                textoTotal += "\t- com processamento nas datas:\n";
                for (Map.Entry<String, Integer> dateout_qntd : objPorCategoria.getMap_dateout_qntd().entrySet()) {
                    String key = dateout_qntd.getKey();
                    textoTotal += "\t\t- " + key + ";\n";
                }
            }
        }
        tab.setText("Total (" + totalCategoria + ")");
        grid.getChildren().clear();
        TextArea textAreaTotal = new TextArea(textoTotal);
        textAreaTotal.setEditable(false);
        textAreaTotal.setWrapText(true);
        grid.add(textAreaTotal, 0, 0);
    }
}
