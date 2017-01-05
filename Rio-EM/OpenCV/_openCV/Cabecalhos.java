package _openCV;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Cabecalhos {

	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		String filename = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-05-06/teste/";

		Mat img = Imgcodecs.imread(filename+"1.jpg");

		int[][] pontosCrop = {{1100,249},
				{1100,462},
				{1532,433},
				{1559,248}};

		int tam_cabec= 1100;
		Mat crop = crop_cabecalho(img, tam_cabec);
		/*int[][] pontosOri = */getPontosCorrespondentes_crop_cabecalho(tam_cabec, pontosCrop, img, crop);
	
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getPontosCorrespondentes_crop_cabecalho<br></b>
	 * Metodo para pegar o ponto correspondente da imagem menor na imagem maior do metodo {@link #crop_cabecalho(Mat, int)}. <br>
	 * @param pontos matriz com os pontos da imagem pequena;
	 * @param orig imagem grande, original.
	 * @param crop imagem pequena, cortada.
	 * @return matriz com os resultante dos pontos da imagem maior.
	 */
	static int[][] getPontosCorrespondentes_crop_cabecalho(int tamcabec, int[][] pontosCrop, Mat orig, Mat crop){
		int[][] final_pontos = new int[pontosCrop.length][pontosCrop[0].length];
		for (int i = 0; i < final_pontos.length; i++) {
			for (int j = 0; j < final_pontos[i].length; j++) {
				if(j==0 && pontosCrop[i][j]>=tamcabec){
					final_pontos[i][j] = pontosCrop[i][j] + (orig.rows()-crop.rows());
				}
				else
					final_pontos[i][j] =  pontosCrop[i][j];
			}
		}
		return final_pontos;

	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo crop_cabecalho</b><br>
	 * Metodo para fazer um crop de cabecalho na parte de cima e de baixo da imagem no padrao {@link Mat}.
	 * @param img imagem em que sera aplicado o crop.
	 * @param tam_cabec tamanho do crop que sera aplicado, tanto para a parte superior quanto inferior da imagem.
	 * @return imagem com o crop.
	 */
	static Mat crop_cabecalho(Mat img, int tam_cabec){
		Mat band = new Mat(tam_cabec*2, img.cols(), img.type());
		for (int i = 0; i < tam_cabec; i++) {
			for (int j = 0; j < img.cols(); j++) {
				double[] x = img.get(i, j);
				band.put(i, j, x);
			}
		}
		int cont= tam_cabec-1;
		for (int i = img.rows()-tam_cabec; i < img.rows(); i++) {
			cont+=1;
			for (int j = 0; j < img.cols(); j++) {
				double[] x = img.get(i, j);
				band.put(cont, j, x);

			}
		}
		return band;
	}
}
