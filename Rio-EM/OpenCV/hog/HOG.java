package hog;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.objdetect.HOGDescriptor;

public class HOG {

	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

	public static void main(String[] args) {
		

		try {
			BufferedImage img =  ImageIO.read(new File("C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-04-03/imgs/230915000031708.png"));
			HOGDescriptor hogDesc = getHogDescriptor(img);
			hogDesc.checkDetectorSize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getHogDescriptor<br></b>
	 * Metodo para pegar os descritores HOG de uma imagem da biblioteca OpenCV.<br>
	 * @param img imagem que sera extraido o descritor;
	 * @return HOGDescriptor.
	 */
	public static HOGDescriptor getHogDescriptor(BufferedImage img){
		HOGDescriptor hogDescriptor =  new HOGDescriptor();
		Mat mat = bufferImgToMat_RGB(img);
		MatOfFloat descriptors = new MatOfFloat();
		hogDescriptor.compute(mat, descriptors);			
		return hogDescriptor;

	}
	
	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo bufferImgToMat_RGB<br></b>
	 * Metodo para converter uma imagem no padrao Java em uma imagem no padrao da biblioteca OpenCV.<br>
	 * @param img imagem a ser convertida;
	 * @return imagem convertida.
	 */
	public static Mat bufferImgToMat_RGB(BufferedImage img) {
		Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}
}
