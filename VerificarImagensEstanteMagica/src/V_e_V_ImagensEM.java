
import com.google.zxing.ResultPoint;
import config.ConfigurarOpenCV;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import models.Itens_Erro;
import models.PDFtoIMG;
import models.QRCodeDetector_AA;
import models.V_e_V_Constantes;

public class V_e_V_ImagensEM extends V_e_V_Constantes{
    public static ConfigurarOpenCV configurarOpenCV = new ConfigurarOpenCV();

    private final double aspect_ratio_limit = 21 / 29.7;
    private final int porcent_aspect_ratio_limit = 5;
    private ArrayList<Itens_Erro> array_erros;
    private int iterador = 0;



    public boolean VV_Imagens(List<File> files, int limitImgs, Tela_LoadController tela_LoadController) throws InterruptedException {
        array_erros = new ArrayList<>();
        try {
            //testa se tem arquivos n�o-imagem no conunto de arquivos
            for (File fl : files) {
                String arq_ext = FilenameUtils.getExtension(fl.getName());//extensao do arquivo
                if (!arq_ext.equalsIgnoreCase("pdf") && !arq_ext.equalsIgnoreCase("bmp") && !arq_ext.equalsIgnoreCase("jpg") && !arq_ext.equalsIgnoreCase("jpeg") && !arq_ext.equalsIgnoreCase("png")) {
                    Itens_Erro e = new Itens_Erro(fl);
                    String msg_erro = MSG_ERRO_SO_PDF;
                    e.addMsgErro(msg_erro);
                    array_erros.add(e);
                    return false;
                }
            }
            //se n�o existir arquivos n�o-imagem ou pdf, faz os testes
            for (File fl : files) {
                String arq_ext = FilenameUtils.getExtension(fl.getName());//extensao do arquivo
                if (arq_ext.equalsIgnoreCase("pdf")) {
                    testPDFs(((fl.getParent()) + File.separator), fl.getName());
                    iterador += 1;
                } else {
                    testes(fl.getName(), ImageIO.read(fl), -1.0, -1.0);
                    iterador += 1;
                }
                Platform.runLater(() -> {
                    tela_LoadController.trocarLabelSubtotalImagens(iterador);
                });
            }
        } catch (Exception ex) {
            Itens_Erro e1 = new Itens_Erro("Erro Desconhecido");
            String msg_erro = MSG_ERRO_DESCONHECIDO;
            e1.addMsgErro(msg_erro);
            array_erros.add(e1);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            return false;
        }
        new Thread().sleep(10000);
        return (array_erros.isEmpty()) ? (true) : (false);
    }

    private void testes(String fileName, BufferedImage img, double real_dpi_w, double real_dpi_h) {
        BufferedImage bufferedImage = img;
        Itens_Erro e = null;
        try {
            double angle = getAngle(bufferedImage);
            if ((angle > LIMIT_ANGLE && angle < 360 - LIMIT_ANGLE && (!(angle > (180) - LIMIT_ANGLE && angle < (180) + LIMIT_ANGLE)))) {
                inserirErro(e, fileName, MSG_ERRO_IMG_TORTA);
            } else if (angle == -1.0) {
                inserirErro(e, fileName, MSG_ERRO_QRCODE_ILEGIVEL);
            } else if (!possuiProporcaoCorreta(img)) {
                inserirErro(e, fileName, MSG_ERRO_ASPECT_RADIO);
            } else if (hasManchas(bufferImgToMat(img))) {
                inserirErro(e, fileName, MSG_ERRO_MANCHAS_FORTES);
            } else {
                //8.17 x 11.69 inches
                //210 x 297 mm
                //21  x 29,7 cm
                double dpi_w = (real_dpi_w == -1.0) ? (bufferedImage.getWidth() / 8.27) : real_dpi_w;
                double dpi_h = (real_dpi_h == -1.0) ? (bufferedImage.getHeight() / 11.69) : real_dpi_h;
                if ((dpi_w < DPI_LIMIT) && (dpi_h < DPI_LIMIT)) {
                    inserirErro(e, fileName, MSG_ERRO_DPI_MIN);
                }
            }
        } catch (Exception ex) {
            inserirErro(e, fileName, MSG_ERRO_DESCONHECIDO);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    private void inserirErro(Itens_Erro e, String filename, String msg_erro) {
        if (e == null) {
            e = new Itens_Erro(filename);
            e.addMsgErro(msg_erro);
            array_erros.add(e);
        } else {
            array_erros.get(array_erros.size() - 1).addMsgErro(msg_erro);
        }
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo pdfToImgs</b><br>
     * Metodo para converter um pdf em arquivos de imgs.
     *
     * @param dirEntrada string que deve conter o somente o diretorio onde esta
     * localizado o arquivo.
     * @param nomeArq_Extens nome do arquivo com a extensao.
     * @param type_file extensao do arquivo;
     * @param id_parent id do pai deste arquivo no BD, se tiver. Se n�o, 0;
     * @param db_dao objeto de conexao com BD.
     * @return resultado booleano da conversao.
     */
    private void testPDFs(String dirEntrada, String nomeArq_Extens) {
        try {
            File fl = new File(dirEntrada + nomeArq_Extens);
            PDFtoIMG pDFtoIMG = new PDFtoIMG();
            List<Image> images = pDFtoIMG.pdfToImgs(fl);
            for (int i = 0; i < images.size(); i++) {
                RenderedImage x = (RenderedImage) images.get(i);
                BufferedImage img = toJPG((toBufferedImage(x)));
                double real_dpi_h = pDFtoIMG.getResolution();
                double real_dpi_w = pDFtoIMG.getResolution();
                String fileName = fl.getName() + " (pagina " + i + ")";
                testes(fileName, img, real_dpi_w, real_dpi_h);
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo toJPG</b><br>
     * Metodo para converter qualquer tipo de extensao de imagem para jpg.
     *
     * @param bufferedImage imagem a ser convertida.
     * @return imagem no padrao jpg.
     */
    private BufferedImage toJPG(BufferedImage bufferedImage) {
        BufferedImage newBufferedImage;//Buffer da img com o aplha
        newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        return newBufferedImage;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private BufferedImage toBufferedImage(RenderedImage img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        ColorModel cm = img.getColorModel();
        int width = img.getWidth();
        int height = img.getHeight();
        WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        Hashtable<String, Object> properties = new Hashtable<>();
        String[] keys = img.getPropertyNames();
        if (keys != null) {
            for (String key : keys) {
                properties.put(key, img.getProperty(key));
            }
        }
        BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
        img.copyData(raster);
        return result;
    }

    private double getAngle(BufferedImage img) {
        try {
            QRCodeDetector_AA qrCodeDetector = new QRCodeDetector_AA();
            ResultPoint[] resultPoints = qrCodeDetector.encontrarQRCode(img);
            if (resultPoints != null && resultPoints.length == 4) {
                Point p1 = ((img.getHeight() > img.getWidth()) ? (new Point((int) resultPoints[2].getX(), (int) resultPoints[2].getY())) : (new Point((int) resultPoints[1].getX(), (int) resultPoints[1].getY())));
                Point p2 = ((img.getHeight() > img.getWidth()) ? (new Point((int) resultPoints[1].getX(), (int) resultPoints[1].getY())) : (new Point((int) resultPoints[0].getX(), (int) resultPoints[0].getY())));
                double angle = Math.toDegrees(Math.atan2(p1.y - p2.y, p1.x - p2.x));
                if (angle < 0) {
                    angle += 360;
                }
                if (angle > 360 - LIMIT_ANGLE) {
                    angle = 360 - angle;
                }
                return angle;
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return -1.0;
    }

    private boolean hasManchas(Mat img) {
        try {
            Mat src = img;
            //Filtro na margem do topo
            int lnIni = 5;
            int lnFin = img.rows() > 85 ? 85 : img.rows();
            Mat subMat = src.submat(lnIni, lnFin, 0, src.width());
            Mat dst_subMat = new Mat();
            Imgproc.blur(subMat, dst_subMat, new Size(KSIZE, KSIZE));
            //Filtro na imagem inteira
            Mat dst = new Mat();
            src = src.submat(lnIni, lnFin, 0, src.width());
            Imgproc.blur(src, dst, new Size(KSIZE, KSIZE));
            src = new Mat();
            src = dst;
            // FAZER O FILTRO DA MEDIA NA IMG ORIG
            double[] src_px;
            for (int i = KSIZE - 1; i < src.rows() - KSIZE; i += KSIZE) {
                for (int j = KSIZE - 1; j < src.cols() - KSIZE; j += KSIZE) {
                    src_px = src.get(i, j);
                    //desconsiderar pixels Brancos e TODO: se o desvio padr�o entre as tres bandas for muito alto(50-100)

                    if ((!ehBranco(src_px)) && desvioPadraoEhAlto(src_px, LIMIT_DESVIO_PADRAO)) {
                        if (possuiPixelIgual(src_px, dst_subMat, MARGEM_ERRO)) {
                            paintVizinhos(i, j, src, KSIZE, new double[]{255, 255, 255});
                        }
                    }
                }
            }
            img = img.submat(0, src.rows(), 0, src.cols());
            long countDif = 0;
            for (int i = KSIZE - 1; i < src.rows() - KSIZE; i += KSIZE) {
                for (int j = KSIZE - 1; j < src.cols() - KSIZE; j += KSIZE) {
                    double[] vet1 = src.get(i, j);
                    double[] vet2 = img.get(i, j);
                    if (!(vetoresSaoIguais(vet1, vet2, MARGEM_ERRO))) {
                        countDif += 1;
                    }
                }
            }
            long max = src.rows() * src.cols();
            long x = ((countDif * 100) / max);
            if (x >= PORCENT_DIFF_MANCHAS) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    private boolean possuiPixelIgual(double[] px, Mat img, int margemErro) {
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
                double[] src_px = img.get(i, j);
                if (vetoresSaoIguais(src_px, px, margemErro)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean vetoresSaoIguais(double[] vet1, double[] vet2, int margemErro) {
        for (int i = 0; i < vet1.length; i++) {
            if (vet1[i] - vet2[i] > margemErro || vet2[i] - vet1[i] > margemErro) {
                return false;
            }
        }
        return true;
    }

    public boolean possuiProporcaoCorreta(BufferedImage img) {
        double aspect_ratio = 0;
        if (img.getWidth() < img.getHeight()) {
            aspect_ratio = Double.valueOf(img.getWidth()) / Double.valueOf(img.getHeight());
        } else {
            aspect_ratio = Double.valueOf(img.getHeight()) / Double.valueOf(img.getWidth());
        }
        double margemErro_Proporcao = (porcent_aspect_ratio_limit * aspect_ratio_limit) / 100;
        double limitMin = aspect_ratio_limit - margemErro_Proporcao;
        double limitMax = aspect_ratio_limit + margemErro_Proporcao;
        return ((aspect_ratio < limitMin || aspect_ratio > limitMax) ? (false) : (true));
    }

    private void paintVizinhos(int i, int j, Mat img, int ksize, double[] RGB) {
        int floorJanela = (int) Math.floor(ksize / 2);
        for (int k = -floorJanela; k <= floorJanela; k++) {
            for (int l = -floorJanela; l <= floorJanela; l++) {
                img.put(k + i, l + j, RGB);
            }
        }
    }

    private boolean desvioPadraoEhAlto(double[] src_px, double limitDesvioPadrao) {
        double somat = 0;
        for (int i = 0; i < src_px.length; i++) {
            somat += src_px[i];
        }
        double media = (somat / src_px.length);
        double var = (((int) (somat - media)) ^ 2) / 255.0;
        double dp = Math.sqrt(var);
        return (dp > limitDesvioPadrao) ? (true) : (false);
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo bufferImgToMat<br></b>
     * Metodo para converter uma imagem no padrao Java em uma imagem no padrao
     * da biblioteca OpenCV.<br>
     *
     * @param img imagem a ser convertida;
     * @return imagem convertida.
     */
    public Mat bufferImgToMat(BufferedImage img) {
        int qntdColorModel = img.getColorModel().getNumColorComponents();
        Mat mat = new Mat(img.getHeight(), img.getWidth(), ((qntdColorModel == 3) ? (CvType.CV_8UC3) : (CvType.CV_8UC1)));
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if (qntdColorModel == 3) {
                    Color color = new Color(img.getRGB(j, i));
                    mat.put(i, j, new double[]{color.getBlue(), color.getGreen(), color.getRed()});
                } else {
                    mat.put(i, j, new double[]{img.getRGB(j, i) & 0xFF});
                }
            }
        }
        return mat;
    }

    /**
     * @return the array_erros
     */
    public ArrayList<Itens_Erro> getArray_erros() {
        return array_erros;
    }

    private boolean ehBranco(double[] src_px) {
        int contBranco = 0;
        for (int i = 0; i < src_px.length; i++) {
            if (src_px[i] >= LIMIAR_BRANCO) {
                contBranco += 1;
            }
        }
        return ((contBranco == src_px.length) ? (true) : (false));
    }

}
