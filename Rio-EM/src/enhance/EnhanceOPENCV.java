package enhance;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class EnhanceOPENCV {
	public static void main( String[] args ) { 

		try {
			System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
			File input = new File("C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\Enhance\\01.jpg");
			BufferedImage image = ImageIO.read(input);	

			long start = System.nanoTime();    
			byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
			Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
			mat.put(0, 0, data);

			Mat mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC1);
			Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

			byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
			mat1.get(0, 0, data1);
			BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
			image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

			long elapsedTime =  System.nanoTime() - start;

			System.out.println(elapsedTime/ 1000000000.0); 
			String dir = "C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\Enhance\\Contrast";
			File ouptut = new File(dir+".jpg");
			ImageIO.write(image1, "jpg", ouptut);

			FileWriter fw =  new FileWriter(dir+".txt");
			fw.write(elapsedTime/ 1000000000.0+"\t segundos\n");
			fw.close();

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}