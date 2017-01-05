package acharlinhapretacabecalho;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class AcharLinhaPretaCabecalho {

    private final static ConfigurarOpenCV CONFIG_OPENCV = new ConfigurarOpenCV();

    public AcharLinhaPretaCabecalho(String filePath) {
        ArrayList<File> listaArq = new ArrayList<>();
        criarListaArqs(new File(filePath), listaArq);
        Iterator<File> iter = listaArq.iterator();
        while (iter.hasNext()) {
            File fl = iter.next();
            Mat dst = eliminarLinhaPretaCabecalho(fl.getAbsolutePath());
            Imgcodecs.imwrite(fl.getAbsolutePath(), dst);
            iter.remove();
        }
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
            System.err.println("Arquivo " + file.getName() + " nao esta completamente escrito.");
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    private Mat eliminarLinhaPretaCabecalho(String filePath) {
        try {
            Mat src = Imgcodecs.imread(filePath);
            double limit_qntdConectivos = src.cols() * 0.85d;
            short limtlinhas = 15;

            Mat gray_dst = src.clone();
            Imgproc.cvtColor(gray_dst, gray_dst, Imgproc.COLOR_RGB2GRAY);
            Mat dst = src.submat(0, limtlinhas, 0, src.cols());

            Mat gray = new Mat();
            Imgproc.cvtColor(dst, gray, Imgproc.COLOR_RGB2GRAY);

            Mat dst_threshold = new Mat();

            Imgproc.threshold(gray, dst_threshold, 127, 255, 0);

            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();

            Imgproc.findContours(dst_threshold, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_KCOS);
            for (int i = 0; i < contours.size(); i++) {
                MatOfPoint pt = contours.get(i);
                double qntdConectivos = Imgproc.contourArea(pt);
                if ((qntdConectivos > limit_qntdConectivos)
                        && ((qntdConectivos / limit_qntdConectivos) <= limtlinhas)) {
                    List<MatOfPoint> cnts = new ArrayList<>();
                    cnts.add(pt);

                    Imgproc.fillPoly(dst_threshold, cnts, new Scalar(255), Imgproc.LINE_8, 0, new Point(0, 0));

                    for (int j = 0; j < limtlinhas - 1; j++) {
                        for (int k = 0; k < dst_threshold.cols(); k++) {
                            if (naoEhAreaContorno(dst_threshold, j, k)) {
                                src.put(j, k, new double[]{255, 255, 255});
                                src.put(j + 1, k, new double[]{255, 255, 255});

                            }
                        }
                    }
                }
            }
            return src;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private boolean naoEhAreaContorno(Mat mask, int j, int k) {
        double[] db = mask.get(j, k);
        return (((db[0] > 225)) ? (false) : (true));
    }

}
