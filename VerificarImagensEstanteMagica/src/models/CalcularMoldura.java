package models;

import com.google.zxing.ResultPoint;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 *
 * @author _
 */
public class CalcularMoldura {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

//	public static void main(String[] args) {
//		String pathname = "C:/Users/_/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-06-23/Imagens_Manchas/"
//			//	+ "capa";
//				+ "z_0";
//		Mat src = Imgcodecs.imread(pathname+".jpg");
//
//		CalcularMoldura calcularMoldura = new CalcularMoldura();
//		if(calcularMoldura.getAngle(matToBufferImg(src))!=-1.0){
//			double real_dpcm_w = (src.cols()/21);
//			double real_dpcm_h = (src.rows()/29.7) ;
//
//			calcularMoldura.hasMolduraPreDef(src, real_dpcm_w, real_dpcm_h);
//		}
//	}
    private String str_QRCodeResult;
    private ResultPoint[] points_resultPoints;

    private void hasMolduraPreDef(Mat src, double real_dpcm_w, double real_dpcm_h) {
        //8.17 x 11.69 inches
        //210 x 297 mm
        //21  x 29,7 cm
        double dpcm_w = real_dpcm_w;
        double dpcm_h = real_dpcm_h;

        //System.out.println("dpcm_w:\t"+dpcm_w);
        //System.out.println("dpcm_h:\t"+dpcm_h);
        double dstcEntrePtControleFimdaPag = 1.50; //cm
        double dstcEntrePtsControleExtremo = 21 - (dstcEntrePtControleFimdaPag * 2); //em cm
        double w_dpcm = (dpcm_w); //dpi real da imagens para dpcm
        dstcEntrePtsControleExtremo *= w_dpcm; //distancia entre pts em cm para dpcm
        //System.out.println("distcPtsControles:\t"+dstcEntrePtsControleExtremo);
        //System.out.println("w_dpcm:\t"+w_dpcm);

        //System.out.println(str_QRCodeResult);
        if (str_QRCodeResult.contains("R") || str_QRCodeResult.contains("r")) {
            System.out.println("R");
            Point pt = new Point((int) points_resultPoints[2].getX(), (int) points_resultPoints[2].getY());
            //ponto controle superior-dir. ate o fim
            int qtndEstimada_Pxs_Margem = (int) (w_dpcm * dstcEntrePtControleFimdaPag);
            System.out.println("qtndPxBorda:\t" + qtndEstimada_Pxs_Margem);
            int distDir = (int) (src.cols() - pt.getX());
            System.out.println("distDir:\t" + distDir);
            int distCima = pt.y;
            System.out.println("distCima:\t" + distCima);
            int distEsq = (int) (src.cols() - (dstcEntrePtsControleExtremo + distDir));
            distEsq = (qtndEstimada_Pxs_Margem + (distEsq + ((qtndEstimada_Pxs_Margem * 2) - distDir)));
            System.out.println("distEsq:\t" + distEsq);

            if (qtndEstimada_Pxs_Margem != distDir || qtndEstimada_Pxs_Margem != distCima || qtndEstimada_Pxs_Margem != distEsq) {
                //TODO: return false;
            } else {
                //TODO: return true;
            }

        } else if (str_QRCodeResult.contains("L") || str_QRCodeResult.contains("l")) {
            System.out.println("L");
            Point pt = new Point((int) points_resultPoints[1].getX(), (int) points_resultPoints[1].getY());
            //ponto controle superioir esq. ate o comeco
            int qtndEstimada_Pxs_Margem = (int) (w_dpcm * dstcEntrePtControleFimdaPag);
            System.out.println("qntdPxBord:\t" + qtndEstimada_Pxs_Margem);

            int distEsq = (int) pt.getX();
            System.out.println("esq:\t" + distEsq);
            int distCima = pt.y;
            System.out.println("cima:\t" + distCima);
            int distDir = (int) (src.cols() - ((dstcEntrePtsControleExtremo + distEsq)));
            distDir = (qtndEstimada_Pxs_Margem + (distDir + (((qtndEstimada_Pxs_Margem * 2)) - distEsq)));
            System.out.println("dir:\t" + distDir);
            if (qtndEstimada_Pxs_Margem != distDir || qtndEstimada_Pxs_Margem != distCima || qtndEstimada_Pxs_Margem != distEsq) {
                //TODO: return false;
            } else {
                //TODO: return true;
            }
        }

    }

    public Mat bufferImgToMat_RGB(BufferedImage img) {
        Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo matToBufferImg<br></b>
     * Metodo para converter uma imagem no padrao da biblioteca OpenCV em uma
     * imagem no padrao Java.<br>
     *
     * @param img imagem a ser convertida;
     * @return imagem convertida.
     */
    public static BufferedImage matToBufferImg(Mat img) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (img.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = img.channels() * img.cols() * img.rows();
        byte[] buffer = new byte[bufferSize];
        img.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(img.cols(), img.
                rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().
                getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }

    private double getAngle(BufferedImage img) {
        QRCodeDetector_AA qrCodeDetector = new QRCodeDetector_AA();
        ResultPoint[] resultPoints = qrCodeDetector.encontrarQRCode(img);
        if (resultPoints != null) {
//			str_QRCodeResult = qrCodeDetector.getStr_result();
//			System.out.println("Result:\t"+str_QRCodeResult);

            points_resultPoints = resultPoints;
            for (int i = 0; i < resultPoints.length; i++) {
                //System.out.println(i);
                //System.out.println(resultPoints[i].toString());
                //System.out.println("\n");
            }
            Point p1 = new Point((int) resultPoints[2].getX(), (int) resultPoints[2].getY());
            Point p2 = new Point((int) resultPoints[1].getX(), (int) resultPoints[1].getY());;

            double angle = Math.toDegrees(Math.atan2(p1.y - p2.y, p1.x - p2.x));

            if (angle < 0) {
                angle += 360;
            }
            return angle;
        } else {
//			//System.out.println("Result:\t"+qrCodeDetector.getStr_result());
//			for ( ResultPoint pts : qrCodeDetector.points()) {
//				//System.out.println(pts.toString());
//			}
            return -1.0;
        }
    }
}
