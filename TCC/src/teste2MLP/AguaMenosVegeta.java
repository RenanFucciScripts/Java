package teste2MLP;

import padrao.MetodosRNA;

public class AguaMenosVegeta {

	public static void main(String[] args) {
		MetodosRNA met =  new MetodosRNA();
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\";
		String dirAgua="C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/NDVI, EVI, NDWI, NIR/Agua/";
		String dirvegeta = "C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\NDVI, EVI, NIR\\Vegeta\\";
		
		int[][] aguaSigmoid=  met.leImagem(dirAgua+"agua (Sigmoid).bmp", "Blue");
		int[][] aguaRamp=  met.leImagem(dirAgua+"agua (Ramp).bmp", "Blue");
		int[][] aguaGaussian=  met.leImagem(dirAgua+"agua (Gaussian).bmp", "Blue");

		int[][] vegetaSigmoid=  met.leImagem(dirvegeta+"vegeta (Sigmoid).bmp", "Green");
		int[][] vegetaRamp=  met.leImagem(dirvegeta+"vegeta (Ramp).bmp", "Green");
		int[][] vegetaGaussian=  met.leImagem(dirvegeta+"vegeta (Gaussian).bmp", "Green");

		int[][] resultSigmoid= new int[aguaSigmoid.length][aguaSigmoid[0].length];

		int[][] resultRamp=  new int[aguaSigmoid.length][aguaSigmoid[0].length];
		
		int[][] resultGaussian=  new int[aguaSigmoid.length][aguaSigmoid[0].length];

		
		for (int i = 0; i < vegetaRamp.length; i++) {
			for (int j = 0; j < vegetaRamp[i].length; j++) {
				if(aguaSigmoid[i][j]>=10 && vegetaSigmoid[i][j]>=10)
					resultSigmoid[i][j]=0;
				else
					resultSigmoid[i][j]=vegetaSigmoid[i][j];
				
				if(aguaGaussian[i][j]>=10 && vegetaGaussian[i][j]>=10)
					resultGaussian[i][j]=0;
				else
					resultGaussian[i][j]=vegetaGaussian[i][j];
				
				if(aguaRamp[i][j]>=10 && vegetaRamp[i][j]>=10)
					resultRamp[i][j]=0;
				else
					resultRamp[i][j]=vegetaRamp[i][j];
			}
		}
		met.gravarImagemGreen(resultGaussian, dir, "resultGaussian");
		met.gravarImagemGreen(resultSigmoid, dir, "resultSigmoid");
		met.gravarImagemGreen(resultRamp, dir, "resultRamp");
		
	//	met.gravarImagemGreen(vegetaRamp, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/NDVI, EVI/Vegeta/", "vegeta (Sigmoid) menos agua");
	}
}
