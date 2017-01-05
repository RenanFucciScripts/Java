package textures;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Textures {
 
	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		
		String filename =  "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-05-18/teste/";
		filename+="Capa (1)_hsl";
		
		Mat src= Imgcodecs.imread(filename+".jpeg");
		
		int ksize = 3;
		LBP lbp = new LBP(ksize);
		
		Mat dst = lbp.calcLBP(src);
		
		Imgcodecs.imwrite(filename+"_lbp("+ksize+").jpeg", dst);
	}
}
