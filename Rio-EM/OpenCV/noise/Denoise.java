package noise;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.sound.sampled.LineUnavailableException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import metodos.MetodosEM;

public class Denoise{

	static{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) throws LineUnavailableException {	

		String filename= "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-04-14/testeEnhance+med_median/8";
		
		try{
			//ImageViewer imageViewer =  new ImageViewer();

			Mat src = Imgcodecs.imread(filename+".jpg", Imgcodecs.IMREAD_COLOR);
			//Imgcodecs.imread(filename+".jpg");

			//			Mat originalImage = src;
			//			Mat grayImage =  new Mat();
			//			Imgproc.cvtColor(originalImage, grayImage, Imgproc.COLOR_BGRA2GRAY);

			//imageViewer.show(src, "src");
			Mat dst =  new Mat();
			//imageViewer.show(src, "src");
			
			/*Imgproc.pyrDown(src, dst, new Size(src.cols()/2,  src.rows()/2));
			src = dst;
			dst =  new Mat();
			Imgproc.pyrDown(src, dst, new Size(src.cols()/2,  src.rows()/2));
			src = dst;
			dst =  new Mat();*/
			long start = System.currentTimeMillis();


			Photo.detailEnhance(src, dst, 10 , 0.75f);
			src = dst;
			dst =  new Mat();
			//Imgproc.medianBlur(src, dst, 3);
			//Imgproc.blur(src, dst, new Size(3, 3));
			Imgproc.bilateralFilter(src, dst, 3, 1.5, 0.4);
			String tempo1 = (((System.currentTimeMillis() - start)/1000.0)+" segundos");
			String filename1 =filename+"_enhance+bilateral_3, 1.5, 0.4";
			Imgcodecs.imwrite(filename1+".jpg", dst);
			criarTxtdoTempo(filename1, tempo1);

			
			//Photo.fastNlMeansDenoisingColored(src, dst, 6, 6, 11, 21);
			//Photo.stylization(src,  dst, 10, 0.05f);
			//System.out.println(((System.currentTimeMillis() - start)/1000.0)+" segundos");
			//imageViewer.show(dst, "dst");

		}catch(Exception e){
			e.printStackTrace();
		}
		MetodosEM.fazerSom();
	}

		
	protected static void criarTxtdoTempo(String fileName, String tempo){
		try {
			String str_tempo = tempo+" segs \n";
			PrintWriter writer = new PrintWriter(fileName+".txt", "UTF-8");
			writer.println(str_tempo);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	
}
