package color_model;

import java.io.File;
import java.util.Stack;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import _openCV._OpenCV;
import metodos.MetodosRF;

public class Color_Model {

	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		MetodosRF metodosRF = new MetodosRF();

		String dir =  "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-05-18/teste_hsl/";
		Stack<File> pilhaArqs = new Stack<File>();
		pilhaArqs = metodosRF.empilharArquivosDiretorio(new File(dir), pilhaArqs);
		System.out.println(pilhaArqs.size());
		
		for (File fl : pilhaArqs) {
			Mat src= Imgcodecs.imread(fl.getAbsolutePath());
			
			Mat h = _OpenCV.separarBanda_Mat(src, 0);
			Mat s = _OpenCV.separarBanda_Mat(src, 1);
			Mat l = _OpenCV.separarBanda_Mat(src, 2);

			Imgcodecs.imwrite(fl.getAbsolutePath().substring(0, fl.getAbsolutePath().length()-5)+"_H.jpeg", h);
			Imgcodecs.imwrite(fl.getAbsolutePath().substring(0, fl.getAbsolutePath().length()-5)+"_S.jpeg", s);
			Imgcodecs.imwrite(fl.getAbsolutePath().substring(0, fl.getAbsolutePath().length()-5)+"_L.jpeg", l);

		}
		
		
	}
	
}
