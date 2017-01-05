package ace;

import java.io.File;
import java.util.Stack;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import metodos.MetodosRF;

public class ACE {

	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
	
		MetodosRF metodosRF = new MetodosRF();
		Stack<File> pilhaArqs = new Stack<>();
		String filename = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-05-22/teste/";
		metodosRF.empilharArquivosDiretorio(new File(filename), pilhaArqs);

		Mat img = Imgcodecs.imread(filename);
		
		int alt = img.height();
		
		
		int perc = (alt*40)/100;
		
		Mat dst = autoEnhance(img, 3);
		
		
	}
	
	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo autoEnhance</b><br>
	 * Metodo para aplicar um metodo automatico de enhance em uma imagem no padrao {@link Mat}.
	 * @param img imagem em que sera aplicado o autoEnhance.
	 * @param intensidade intensidade no metodo.
	 * @return imagem com o metodo autoEnhance.
	 */
	static Mat autoEnhance(Mat img, int intensidade){
		try{
			Mat src = img;
			
			Mat R =  separarBanda_Mat(src, 0);
			Mat G =  separarBanda_Mat(src, 1);
			Mat B =  separarBanda_Mat(src, 2);

			Mat R_dst = new Mat();
			Mat G_dst = new Mat();
			Mat B_dst = new Mat();

			CLAHE clahe = Imgproc.createCLAHE();
			clahe.setClipLimit(intensidade);
			clahe.apply(R, R_dst);
			clahe.apply(G, G_dst);
			clahe.apply(B, B_dst);


			return juntarBandas_Mat(R_dst, G_dst, B_dst);
		}
		catch(Exception ex){ 
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		
		return null;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo separarBanda_Mat</b><br>
	 * Metodo para separar uma imagem em uma so banda especifica dentre RGB, na qual 0=RED, 1=GREEN e 2=BLUE.
	 * @param img imagem a ser separada no padrao {@link Mat}.
	 * @param banda banda especifica dentre RGB, na qual 0=RED, 1=GREEN e 2=BLUE.
	 * @return imagem separada.
	 */
	static Mat separarBanda_Mat(Mat img, int banda){
		Mat band = new Mat(img.size(), CvType.CV_8UC1);
		for (int i = 0; i < img.rows(); i++) {
			for (int j = 0; j < img.cols(); j++) {
				double[] data = img.get(i, j);
				band.put(i, j, data[banda]);
			}
		}
		return band;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo juntarBandas_Mat</b><br>
	 * Metodo para juntar tres {@link Mat}, isto e, tres bandas de imagens (R, G e B)  uma imagem em uma so imagem no padrao {@link Mat}.
	 * @param band_1 banda R no padrao {@link Mat}.
	 * @param band_2 banda G no padrao {@link Mat}.
	 * @param band_3 banda B no padrao {@link Mat}.
	 * @return imagem colorida, isto é, juncao das bandas.
	 */
	static Mat juntarBandas_Mat(Mat band_1, Mat band_2, Mat band_3){
		Mat img = new Mat(band_1.size(), CvType.CV_8UC3);
		for (int i = 0; i < img.rows(); i++) {
			for (int j = 0; j < img.cols(); j++) {
				double[] rs = band_1.get(i, j);
				double[] gs = band_2.get(i, j);
				double[] bs = band_3.get(i, j);

				double[] data =  {rs[0], gs[0], bs[0]};
				img.put(i, j, data);

			}
		}
		return img;
	}


}
