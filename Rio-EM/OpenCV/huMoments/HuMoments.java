package huMoments;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class HuMoments {


	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}


	public static void main(String[] args) throws IOException {


		BufferedImage img =  ImageIO.read(new File("C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-04-03/imgs/230915000031708.png"));

		getHuMoments(img);

	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getHuMoments</b><br>
	 * Metodo para pegar os Hu Moments de uma imagem.
	 * @param img imagem que serao extraidos os huMoments.
	 * @return vetor de double com os Hu Moments.
	 */
	public static double[] getHuMoments(BufferedImage img){
		Moments moments = new Moments();
		moments = Imgproc.moments(bufferImgToMat_Gray(img), true);
		Mat hu = new Mat();
		Imgproc.HuMoments(moments, hu);
		System.out.println(moments);
		System.out.println(hu.cols());
		System.out.println(hu.rows());
		double[] huMoments= new double[7]; 
		hu.get(0, 0, huMoments);
		
		return huMoments;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo bufferImgToMat_Gray<br></b>
	 * Metodo para converter uma imagem no padrao Java em uma imagem no padrao da biblioteca OpenCV com uma so banda.<br>
	 * @param img imagem a ser convertida;
	 * @return imagem convertida.
	 */
	public static Mat bufferImgToMat_Gray(BufferedImage img) {
		Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8U);
		byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

}
