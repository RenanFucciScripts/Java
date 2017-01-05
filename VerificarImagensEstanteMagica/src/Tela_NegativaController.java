
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import models.Itens_Erro;

/**
 * FXML Controller class
 *
 * @author _
 */
public class Tela_NegativaController implements Initializable {

    public static final String msg_erro_desconhecido
            = "Oops! Tivemos um problema estranho que não conseguimos identificar."
            + "\nÉ necessário entrar em contato com a Estante Mágica enviando esse arquivo de imagem e um txt chamado \"Erro-Desconhecido.txt\" que está na mesma pasta desse aplicativo, tudo bem? "
            + "\nÉ importante enviar essas informações para que possamos ajudar na resolução definitiva do problema.";
    @FXML
    public ScrollPane scrollPaneNegativo;

    @FXML
    public Button buttonVerificarNovamente;

    private Map<String, ImageIcon> imageMap;
    private Stage mainStage;
    public ArrayList<Itens_Erro> arrayList_ItensErro;

    public Tela_NegativaController(Stage stage, ArrayList<Itens_Erro> arrayList_ItensErro) {
        this.arrayList_ItensErro = arrayList_ItensErro;
        this.mainStage = stage;

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadItens();
    }

    @FXML
    public void onClickButtonVerificarNovamente() {
        Stage stage = (Stage) buttonVerificarNovamente.getScene().getWindow();
        NovoFXMain.reStart(stage);
    }

    private void loadItens() {
        try {
            scrollPaneNegativo.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPaneNegativo.setFitToWidth(true);

            VBox vBox = new VBox();
            vBox.setStyle("-fx-background-color: rgb(154,210,232);");
            for (int i = 0; i < arrayList_ItensErro.size(); i++) {
                int aux = i + 1;
                Itens_Erro items2 = arrayList_ItensErro.get(i);
                ImageView imageView = new ImageView(SwingFXUtils.toFXImage(criarImagemItem(aux + ""), null));
                String texto_oops = "Arquivo: " + items2.getFileName();
                for (String string : items2.getMsgs_erro()) {
                    texto_oops += "\n" + string;
                }

                Label label = new Label(texto_oops);
                label.setWrapText(true);
                label.setFont(new Font("Tahoma", 14));
                label.setTextFill(Color.rgb(3, 109, 175));
                GridPane gridPane = new GridPane();

                gridPane.add(imageView, 0, 0);
                gridPane.setMargin(imageView, new Insets(25, 15, 0, 15));
                gridPane.add(label, 1, 0);
                gridPane.setMargin(label, new Insets(25, 0, 0, 0));

                vBox.getChildren().add(gridPane);
            }
            scrollPaneNegativo.setStyle("-fx-background-color: rgb(154,210,232);");
            scrollPaneNegativo.setContent(vBox);
        } catch (Error er) {
            atualizarErro();
            Tela_InicialController.criarTxtErro(er);
        } catch (Exception ex) {
            atualizarErro();
            Tela_InicialController.criarTxtErro(ex);
        }

    }

    private void atualizarErro() {

        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: rgb(154,210,232);");
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(criarImagemItem("1"), null));
        String texto_oops = "Arquivo: :(\n" + msg_erro_desconhecido;
        Label label = new Label(texto_oops);
        label.setWrapText(true);
        label.setFont(new Font("Tahoma", 14));
        label.setTextFill(Color.rgb(3, 109, 175));
        GridPane gridPane = new GridPane();

        gridPane.add(imageView, 0, 0);
        gridPane.setMargin(imageView, new Insets(25, 15, 0, 15));
        gridPane.add(label, 1, 0);
        gridPane.setMargin(label, new Insets(25, 0, 0, 0));

        vBox.getChildren().add(gridPane);
        scrollPaneNegativo.setStyle("-fx-background-color: rgb(154,210,232);");
        scrollPaneNegativo.setContent(vBox);
    }

    public BufferedImage criarImagemItem(String text) {
        try {
            int tam = Integer.parseInt(text);

            BufferedImage estrela = ImageIO.read(getClass().getResource("/imgs/icon-06-itens.png"));

            BufferedImage img = new BufferedImage(estrela.getWidth(), estrela.getHeight(), estrela.getType());
            Graphics2D g2d = img.createGraphics();
            java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 20);
            if (tam >= 100) {
                font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 16);
            }
            g2d.setFont(font);
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            int width = fm.stringWidth(text);
            int height = fm.getHeight();
            g2d.dispose();

            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2d.setFont(font);
            fm = g2d.getFontMetrics();
            g2d.setColor(new java.awt.Color(228, 179, 0));
            g2d.drawString(text, 0, fm.getAscent());
            g2d.dispose();

            BufferedImage combined = new BufferedImage(estrela.getWidth(), estrela.getHeight(), estrela.getType());

            Graphics g = combined.getGraphics();
            if (tam < 10) {
                g.drawImage(estrela, 0, 0, null);
                g.drawImage(img, 18, 14, null);
            } else if (tam >= 10 && tam < 100) {
                g.drawImage(estrela, 0, 0, null);
                g.drawImage(img, 13, 14, null);
            } else {
                g.drawImage(estrela, 0, 0, null);
                g.drawImage(img, 10, 15, null);
            }
            return combined;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
