package alinhartextos_deskew_em;

import acharlinhapretacabecalho.ConfigurarOpenCV;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * TODO:
 * - https://www.tutorialspoint.com/dip/prewitt_operator.htm
 * - deslocar o pixel pra direita, fazer a media pelo pixel da direita.
 *  EX.: se estou na coord. 10;10, subistituo o 10;11 pela media do 10;10 e do 10;11. Só na horizontal;
 */
public class AlinharTextos_Deskew_EM extends Imgproc {

    private final static ConfigurarOpenCV CONFIG_OPENCV = new ConfigurarOpenCV();
    private final int ksize = 11;
    private final int erodeKsize = 3;
    private final int erodeIteration = 7;

    private int tamMinLinha;
    private int tamMaxGap;
    private boolean probabilistica = false;
    private String str_filtro;

    public static void main(String[] args) {
        String dir = "C:/Users/_/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-10-19/teste_imgs_em/";
        new AlinharTextos_Deskew_EM(dir);
        //new AlinharTextos_Deskew_EM(dir, true, 100, 30);
    }

    /**
     * @deprecated
     *
     *
     */
    public AlinharTextos_Deskew_EM(String filePath) {
        percorrerImagens(filePath);
    }

    public AlinharTextos_Deskew_EM(String filePath, boolean probabilistica, int tamMinLinha, int tamMaxGap) {
        this.tamMinLinha = tamMinLinha;
        this.tamMaxGap = tamMaxGap;
        this.probabilistica = probabilistica;
        percorrerImagens(filePath);

    }

    private void percorrerImagens(String filePath) {
        ArrayList<File> listaArq = new ArrayList<>();
        criarListaArqs(new File(filePath), listaArq);
        Iterator<File> iter = listaArq.iterator();
        while (iter.hasNext()) {
            File fl = iter.next();
            Mat dst = alinharImages(fl.getAbsolutePath());
            Imgcodecs.imwrite(fl.getAbsolutePath().substring(0,
                    fl.getAbsolutePath().lastIndexOf(".")) + str_filtro + ksize + ")_hough"
                    + (((probabilistica) ? ("_p(minLN=" + tamMinLinha + ", maxGAP=" + tamMaxGap + ")") : (""))) + ".png",
                    dst);

            iter.remove();
        }

    }

    private Mat alinharImages(String absolutePath) {
        Mat img = Imgcodecs.imread(absolutePath);
        img = img.submat(0, ((int) (img.rows() * 0.3d)), 0, img.cols());
        Mat dst_filters = new Mat();
        tamMaxGap = img.cols();
        medianBlur(img, dst_filters, ksize);
        img = dst_filters;
        Mat element_kernel = getStructuringElement(MORPH_RECT, new Size(erodeKsize, erodeKsize));
        erode(img, dst_filters, element_kernel, new Point(-1, -1), erodeIteration);
        str_filtro = "_erode(ksize_" + erodeKsize + ", ite_" + erodeIteration + ")_mean(";
//        Imgcodecs.imwrite(absolutePath.substring(0, absolutePath.lastIndexOf(".")) 
//                + str_filtro + ksize + ").png", dst_filters);
        Mat dst_lines;
        if (probabilistica) {
            dst_lines = transformadaHoughLinhaProbabilistica(dst_filters);
        } else {
            dst_lines = transformadaHoughLinha(dst_filters);
        }
        return dst_lines;

    }

    private Mat transformadaHoughLinhaProbabilistica(Mat img) {
        Mat dst_canny = preProcessHoughLinha(img);

        Mat lines = new Mat();
        double rho = 1, theta = Math.PI / 180.0;
        int threshold = 200,
                minLineLength = (tamMinLinha <= 0) ? (100) : (tamMinLinha),
                maxLineGap = (tamMaxGap <= 0) ? (10) : (tamMaxGap);
        HoughLinesP(dst_canny, lines, rho, theta, threshold, minLineLength, maxLineGap);

        for (int i = 0; i < lines.rows(); i++) {
            double[] data = lines.get(i, 0);
            int x1 = (int) data[0];
            int y1 = (int) data[1];
            int x2 = (int) data[2];
            int y2 = (int) data[3];
            double difY = Math.abs(y2 - y1);//Linhas Horizontais
            if (difY < 10) { //<10 margem de erro, com metade valor do angulo aceito no HARPIA
                line(img, new Point(x1, y1), new Point(x2, y2), new Scalar(0, 255, 0), 2);
            }
        }
        return img;
    }

    /**
     * @deprecated 
     *
     */
    private Mat transformadaHoughLinha(Mat img) {
        Mat dst_canny = preProcessHoughLinha(img);

        Mat lines = new Mat();
        double rho = 1, theta = Math.PI / 180.0;
        int threshold = 250;
        HoughLines(dst_canny, lines, rho, theta, threshold);

        for (int i = 0; i < lines.rows(); i++) {
            double[] data = lines.get(i, 0);
            rho = data[0];
            theta = data[1];
            double a = Math.cos(theta);
            double b = Math.sin(theta);
            double x0 = a * rho;
            double y0 = b * rho;
            int x1 = (int) (x0 + 1000.0 * (-b));
            int y1 = (int) (y0 + 1000.0 * (a));
            int x2 = (int) (x0 - 1000.0 * (-b));
            int y2 = (int) (y0 - 1000.0 * (a));

            line(img, new Point(x1, y1), new Point(x2, y2), new Scalar(0, 255, 0), 2);

        }
        return img;
    }

    private Mat preProcessHoughLinha(Mat img) {
        Mat img_aux = new Mat();
        if (img_aux.type() != CvType.CV_8UC1) {
            cvtColor(img, img_aux, COLOR_RGB2GRAY);
        } else {
            img_aux = img.clone();
        }
        Mat dst_canny = new Mat();
        Canny(img_aux, dst_canny, 50, 150);
        return dst_canny;
    }

    private void criarListaArqs(File folder, ArrayList<File> listaArq) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                criarListaArqs(fileEntry, listaArq);
            } else if (fileIsComplete(fileEntry)) {
                listaArq.add(fileEntry);
            }
        }
    }

    private boolean fileIsComplete(File file) {
        try (RandomAccessFile stream = new RandomAccessFile(file, "rw");) {
            return true;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    "Arquivo " + file.getName() + " nao esta completamente escrito.\n" + ex.getMessage(), ex);
            return false;
        }
    }

}
